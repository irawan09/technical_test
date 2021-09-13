package com.electroshock.technicaltestandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.electroshock.technicaltestandroid.MainActivity.HexToJetpackColor.getColor
import com.electroshock.technicaltestandroid.model.DataService
import com.electroshock.technicaltestandroid.ui.theme.TechnicalTestAndroidTheme
import com.electroshock.technicaltestandroid.view_model.DataViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val sharedPrefFile = "SharedPreference"
    var card_image = CardImageData()
    val dataPojo = Data()
    var itemsArray1: ArrayList<ImageData> = ArrayList()
    var itemsArray2: ArrayList<TitleImageData> = ArrayList()
    var itemsArray3: ArrayList<CardImageData> = ArrayList()

    private lateinit var viewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContext(this)
        if (isNetworkAvailable(applicationContext) == true){
            parseJSON()
        }else {
            Toast.makeText(this@MainActivity, "Please turn on your internet Connection", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    @SuppressLint("LongLogTag")
    private fun parseJSON() {

        Log.d("DataFragment", "Called DataModelProvider.get")
        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        // Create Retrofit object
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Service for retrofit object
        val service = retrofit.create(DataService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            // Do the GET request and get response
            val response = service.getData()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(response.body())
                    Log.d("Pretty JSON response ", prettyJson)

                    val items = response.body()?.pages?.cards
                    if (items != null) {

                        for (i in 0 until items.count()) {

                            if (i <= items.count()) {

                                // as condition in statement if, I am more prefer
                                // to use the index of  of the response from JSON value than
                                // using the card_type value because from my understanding,
                                // there is anomaly on the second card structure to present it
                                // as nice structure in Recycle view
                                if (i == 0) {
                                    //Working for header part
                                    var headerJsonValue = items[i].card?.cardValue ?: "N/A"
                                    dataPojo.headerValue = headerJsonValue
                                    Log.d("Header value: ", dataPojo.headerValue)

                                    var headerJsonTextColor = items[i].card?.cardAttributes?.attributesTextColor
                                        ?: "N/A"
                                    dataPojo.headerTextColor = headerJsonTextColor
                                    Log.d("Header Text Color: ", dataPojo.headerTextColor)

                                    var headerJsonTextSize = (items[i].card?.cardAttributes?.attributesFont?.attributesFontSize
                                        ?: "N/A").toString()
                                    dataPojo.headerFontSize = headerJsonTextSize
                                    Log.d("Header Text Size: ", dataPojo.headerFontSize)

                                } else if (i == 1) {

                                    //Working for subheader part
                                    var subHeaderJsonValue = items[i].card?.cardTitle?.titleValue
                                        ?: "N/A"
                                    dataPojo.subHeaderValue = subHeaderJsonValue
                                    Log.d("SubHeader value: ", dataPojo.subHeaderValue)

                                    var subHeaderJsonTextColor = items[i].card?.cardTitle?.titleAttributes?.titleAttributesTextColor
                                        ?: "N/A"
                                    dataPojo.subHeaderTextColor = subHeaderJsonTextColor
                                    Log.d("SubHeader Text Color: ", dataPojo.subHeaderTextColor)

                                    var subHeaderJsonTextSize = (items[i].card?.cardTitle?.titleAttributes?.titleAttributesSize?.size
                                        ?: "N/A").toString()
                                    dataPojo.subHeaderFontSize = subHeaderJsonTextSize
                                    Log.d("SubHeader Text Size: ", dataPojo.subHeaderFontSize)

                                    //Working for description subheader part
                                    var descSubHeaderJsonValue = items[i].card?.cardDescription?.descriptionValue
                                        ?: "N/A"
                                    dataPojo.descSubHeaderValue = descSubHeaderJsonValue
                                    Log.d("DescSubHeader value: ", dataPojo.descSubHeaderValue)

                                    var descSubHeaderJsonTextColor = items[i].card?.cardDescription?.descriptionAttributes?.descriptionAttributesTextColor
                                        ?: "N/A"
                                    dataPojo.descSubHeaderTextColor = descSubHeaderJsonTextColor
                                    Log.d("DescSubHeader Text Color: ", dataPojo.descSubHeaderTextColor)

                                    var descSubHeaderJsonTextSize = (items[i].card?.cardDescription?.descriptionAttributes?.descriptionAttributesFontSize?.size
                                        ?: "N/A").toString()
                                    dataPojo.descSubHeaderFontSize = descSubHeaderJsonTextSize
                                    Log.d("DescSubHeader Text Size: ", dataPojo.descSubHeaderFontSize)

                                } else if (i == 3 || i == 5) {

                                    // Card Title
                                    val cardTitle = items[i].card?.cardTitle?.titleValue ?: "N/A"
                                    Log.d("Title: ", cardTitle)

                                    // Card Description
                                    val cardDescription = items[i].card?.cardDescription?.descriptionValue
                                        ?: "N/A"
                                    Log.d("Description: ", cardDescription)

                                    //Card Title text color
                                    val cardTitleTextColor = items[i].card?.cardTitle?.titleAttributes?.titleAttributesTextColor
                                        ?: "N/A"
                                    Log.d("Card Title Text Color: ", cardTitleTextColor)

                                    //Card Title Text Size
                                    val cardTitleTextSize = items[i].card?.cardTitle?.titleAttributes?.titleAttributesSize?.size
                                        ?: "N/A"
                                    Log.d("Card Title Text Size: ", cardTitleTextSize)

                                    //Card Description Text Size
                                    val cardDescriptionTextColor = items[i].card?.cardDescription?.descriptionAttributes?.descriptionAttributesTextColor
                                                    ?: "N/A"
                                    Log.d("Card Description Text Color ", cardDescriptionTextColor)
                                    //Card Description Text Size
                                    val cardDescriptionTextSize = items[i].card?.cardDescription?.descriptionAttributes?.descriptionAttributesFontSize?.size
                                        ?: "N/A"
                                    Log.d("Card Description Text Size ", cardDescriptionTextSize)

                                    val titleImageData =
                                        TitleImageData(
                                            cardTitle,
                                            cardTitleTextColor,
                                            cardTitleTextSize,
                                            cardDescription,
                                            cardDescriptionTextColor,
                                            cardDescriptionTextSize
                                        )
                                    // I am store the title_description card type in a
                                    // single array.
                                    itemsArray2.add(titleImageData)
                                    Log.d("Array 2 saya ", itemsArray2.toString())

                                } else {
                                    // Card Title
                                    val cardTitle = items[i].card?.cardTitle?.titleValue ?: "N/A"
                                    Log.d("Card Title ", cardTitle)
                                    // Card Description
                                    val cardDescription = items[i].card?.cardDescription?.descriptionValue
                                        ?: "N/A"
                                    Log.d("Card Description ", cardDescription)
                                    // Card Image
                                    val cardImage = items[i].card?.cardImage?.imageUrl ?: "N/A"
                                    dataPojo.imageUrl = cardImage
                                    Log.d("Card Image ", cardImage)
                                    //Card Title text color
                                    val cardTitleTextColor = items[i].card?.cardTitle?.titleAttributes?.titleAttributesTextColor
                                        ?: "N/A"
                                    Log.d("Card Title Text Color ", cardTitleTextColor)
                                    //Card Title Text Size
                                    val cardTitleTextSize = items[i].card?.cardTitle?.titleAttributes?.titleAttributesSize?.size
                                        ?: "N/A"
                                    Log.d("Card Title Text Size ", cardTitleTextSize)
                                    //Card Description Text Size
                                    val cardDescriptionTextColor = items[i].card?.cardDescription?.descriptionAttributes?.descriptionAttributesTextColor
                                        ?: "N/A"
                                    Log.d("Card Description Text Color: ", cardDescriptionTextColor)
                                    //Card Description Text Size
                                    val cardDescriptionTextSize = items[i].card?.cardDescription?.descriptionAttributes?.descriptionAttributesFontSize?.size
                                        ?: "N/A"
                                    Log.d("Card Description Text Size: ", cardDescriptionTextSize)
                                    //Card Image width
                                    val cardImageWidth = items[i].card?.cardImage?.imageSize?.imageSizeWidth
                                        ?: "N/A"
                                    dataPojo.imageSizeWidth = cardImageWidth
                                    Log.d("Card Image Width ", cardImageWidth)
                                    //Card Image height
                                    val cardImageHeight = items[i].card?.cardImage?.imageSize?.imageSizeHeight
                                        ?: "N/A"
                                    dataPojo.imageSizeHeight = cardImageHeight
                                    Log.d("Card Image Height ", cardImageHeight)

                                    val image =
                                        ImageData(
                                            cardImage,
                                            cardImageWidth,
                                            cardImageHeight,
                                            cardTitle,
                                            cardTitleTextColor,
                                            cardTitleTextSize,
                                            cardDescription,
                                            cardDescriptionTextColor,
                                            cardDescriptionTextSize
                                        )

                                    // I am store the image_description_title card type in a
                                    // single array.
                                    itemsArray1.add(image)
                                    Log.d("Array 1 saya ", itemsArray1.toString())

                                }
                            }
                        }
                    }

                    val sharedPreferences: SharedPreferences = getContext().getSharedPreferences(sharedPrefFile,Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                    editor.putString("headerValue",dataPojo.headerValue)
                    editor.putString("headerColor",dataPojo.headerTextColor)
                    editor.putString("headerSize",dataPojo.headerFontSize)
                    editor.apply()
                    editor.commit()

                    // After store all the data on different array
                    // then I joined all the array into one Array to get the data
                    // structure from the requirements.
                    for (j in 0 until itemsArray2.size){
                        var data1 = itemsArray2[j].cardTitle
                        var data2 = itemsArray2[j].cardTextColor
                        var data3 = itemsArray2[j].cardTextSize
                        var data4 = itemsArray2[j].cardDescription
                        var data5 = itemsArray2[j].cardDescriptionTextColor
                        var data6 = itemsArray2[j].cardDescriptionTextSize
                        var data7 = itemsArray1[j].cardImage
                        var data8 = itemsArray1[j].cardImageWidth
                        var data9 = itemsArray1[j].cardImageHeight
                        var data10 = itemsArray1[j].cardImageTitle
                        var data11 = itemsArray1[j].cardImageTitleTextColor
                        var data12 = itemsArray1[j].cardImageTitleTextSize
                        var data13 = itemsArray1[j].cardImageDescription
                        var data14 = itemsArray1[j].cardImageDescriptionTextColor
                        var data15 = itemsArray1[j].cardImageDescriptionTextSize

                        card_image =
                            CardImageData(
                                data1,
                                data2,
                                data3,
                                data4,
                                data5,
                                data6,
                                data7,
                                data8,
                                data9,
                                data10,
                                data11,
                                data12,
                                data13,
                                data14,
                                data15
                            )
                        itemsArray3.add(card_image)
                        Log.d("Array 3 saya ", itemsArray3.toString())

                        viewModel.add(card_image)
                        Log.d("Data View Model saya ", viewModel.toString())

                    }

                    setContent {
                        TechnicalTestAndroidTheme {
                            // A surface container using the 'background' color from the theme
                            Surface(color = MaterialTheme.colors.background) {
                                Scaffold{
                                    Column(
                                        verticalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentSize(Alignment.Center)) {
                                        TitleCard(Header(dataPojo.headerValue, dataPojo.headerTextColor, dataPojo.headerFontSize))
                                        TitleCard(Header(dataPojo.subHeaderValue, dataPojo.subHeaderTextColor, dataPojo.subHeaderFontSize))
                                        TitleCard(Header(dataPojo.descSubHeaderValue, dataPojo.descSubHeaderTextColor, dataPojo.descSubHeaderFontSize))
                                        CardList(itemsArray3)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
    companion object {
        var baseUrl = "https://private-8ce77c-tmobiletest.apiary-mock.com/"

        private lateinit var context: Context

        fun setContext(con: Context) {
            context=con
        }

        fun getContext(): Context {
            return context
        }
    }

    object HexToJetpackColor {
        fun getColor(colorString: String): Color {
            return Color(android.graphics.Color.parseColor(colorString))
        }
    }

}

@Composable
fun TitleCard(header: Header){
    TechnicalTestAndroidTheme{
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable(onClick = { })){

            Text(header.cardTitle, color = getColor(header.cardTextColor), fontSize = ((header.cardTextSize).toInt()).sp)
        }
    }
}


@Composable
fun CardList(cardList : List<CardImageData>){
    LazyColumn {
        items(cardList) { cardList ->
            Text(cardList.cardTitle, color = getColor(cardList.cardTextColor), fontSize = (((cardList.cardTextSize).toInt()).sp))
            Text(cardList.cardDescription, color = getColor(cardList.cardDescriptionTextColor), fontSize = (((cardList.cardDescriptionTextSize).toInt()).sp))
            LoadImage(url = cardList.cardImage, heightImage = cardList.cardImageHeight, widthImage = cardList.cardImageWidth, imageTitle = cardList.cardImageTitle, imageTitleTextColor = cardList.cardImageTitleTextColor, imageTitleTextSize = cardList.cardImageTitleTextSize, imageDescription = cardList.cardImageDescription, imageDescriptionTextColor = cardList.cardImageDescriptionTextColor, imageDescriptionTextSize = cardList.cardImageDescriptionTextSize)
        }
    }
}

@Composable
fun LoadImage(
    url: String,
    heightImage : String,
    widthImage : String,
    imageTitle : String,
    imageTitleTextColor : String,
    imageTitleTextSize : String,
    imageDescription : String,
    imageDescriptionTextColor : String,
    imageDescriptionTextSize : String ){

    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .clickable(onClick = { })
        .clip(shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center) {
            val painter = rememberImagePainter(
                data = url,
                builder = {

                }
            )

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .height((heightImage.toInt()).dp)
                .width((widthImage.toInt()).dp))
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .height(350.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.BottomStart)) {
            Text(
                imageTitle,
                color = getColor(imageTitleTextColor),
                fontSize = ((imageTitleTextSize).toInt()).sp
            )
            Text(
                imageDescription,
                color = getColor(imageDescriptionTextColor),
                fontSize = ((imageDescriptionTextSize).toInt()).sp
            )
        }
    }
}
