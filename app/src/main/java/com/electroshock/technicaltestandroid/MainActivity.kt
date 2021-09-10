package com.electroshock.technicaltestandroid

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.electroshock.technicaltestandroid.MainActivity.HexToJetpackColor.getColor
import com.electroshock.technicaltestandroid.ui.theme.TechnicalTestAndroidTheme
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private val sharedPrefFile = "kotlinsharedpreference"
    val dataPojo = Data()
    var itemsArray1: ArrayList<Cell> = ArrayList()
    var itemsArray2: ArrayList<Cell> = ArrayList()

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
                    Log.d("Pretty Printed JSON :", prettyJson)

                    val items = response.body()?.pages?.cards
                    if (items != null) {

                        for (i in 0 until items.count()) {
                            Log.d("Number of data : ", items.count().toString())

                            if (i <= items.count()) {

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

                                    // Card Image
                                    val cardImage = items[i].card?.cardImage?.imageUrl ?: "N/A"
                                    Log.d("Card Image: ", cardImage)

                                    //Card Title/Description text color
                                    val cardTitleTextColor = items[i].card?.cardAttributes?.attributesTextColor
                                        ?: "N/A"
                                    Log.d("Card Title Text Color: ", cardTitleTextColor)

                                    //Card Title/Description Text Size
                                    val cardTitleTextSize = items[i].card?.cardAttributes?.attributesFont?.attributesFontSize
                                        ?: "N/A"
                                    Log.d("Card Title Text Size: ", cardTitleTextSize)

                                    //Card Image width
                                    val cardImageWidth = items[i].card?.cardImage?.imageSize?.imageSizeWidth
                                        ?: "N/A"
                                    Log.d("Card Title Text Color: ", cardImageWidth)

                                    //Card Image height
                                    val cardImageHeight = items[i].card?.cardImage?.imageSize?.imageSizeHeight
                                        ?: "N/A"
                                    Log.d("Card Title Text Color: ", cardImageHeight)

                                    val image =
                                        ImageData(
                                            cardImage,
                                            cardImageWidth,
                                            cardImageHeight
                                        )
//                                    itemsArray1.add(modelCard)
//                                    Log.d("Array 1 saya ", itemsArray1.toString())

                                } else {
                                    // Card Title
                                    val cardTitle = items[i].card?.cardTitle?.titleValue ?: "N/A"
                                    // Card Description
                                    val cardDescription = items[i].card?.cardDescription?.descriptionValue
                                        ?: "N/A"
                                    // Card Image
                                    val cardImage = items[i].card?.cardImage?.imageUrl ?: "N/A"
                                    dataPojo.imageUrl = cardImage
                                    //Card Title/Description text color
                                    val cardTitleTextColor = items[i].card?.cardAttributes?.attributesTextColor
                                        ?: "N/A"
                                    //Card Title/Description Text Size
                                    val cardTitleTextSize = items[i].card?.cardAttributes?.attributesFont?.attributesFontSize
                                        ?: "N/A"
                                    //Card Image width
                                    val cardImageWidth = items[i].card?.cardImage?.imageSize?.imageSizeWidth
                                        ?: "N/A"
                                    dataPojo.imageSizeWidth = cardImageWidth
                                    //Card Image height
                                    val cardImageHeight = items[i].card?.cardImage?.imageSize?.imageSizeHeight
                                        ?: "N/A"
                                    dataPojo.imageSizeHeight = cardImageHeight

//                                    val modelCard =
//                                        Cell(
//                                            cardTitle,
//                                            cardDescription,
//                                            cardImage,
//                                            cardTitleTextColor,
//                                            cardTitleTextSize,
//                                            cardImageWidth,
//                                            cardImageHeight
//                                        )
//                                    itemsArray2.add(modelCard)
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

                    setContent {
                        TechnicalTestAndroidTheme {
                            // A surface container using the 'background' color from the theme
                            Surface(color = MaterialTheme.colors.background) {
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentSize(Alignment.Center)) {
                                    TitleCard(Cell(dataPojo.headerValue, dataPojo.headerTextColor, dataPojo.headerFontSize))
                                    TitleCard(Cell(dataPojo.subHeaderValue, dataPojo.subHeaderTextColor, dataPojo.subHeaderFontSize))
                                    TitleCard(Cell(dataPojo.descSubHeaderValue, dataPojo.descSubHeaderTextColor, dataPojo.descSubHeaderFontSize))
                                    CoilImage(dataPojo.imageUrl, dataPojo.imageSizeHeight, dataPojo.imageSizeWidth)
                                }
                                LazyColumn {

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
fun TitleCard(cell: Cell){
    TechnicalTestAndroidTheme{
        Column(modifier = Modifier
//            .padding(.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable(onClick = { } )){
//            .clip(shape = RoundedCornerShape(16.dp))){

            Text(cell.cardTitle, color = getColor(cell.cardTextColor), fontSize = ((cell.cardTextSize).toInt()).sp)
        }
    }
}

@Composable
fun CoilImage(url: String, heightImage : String, widthImage : String){
    Box(modifier = Modifier
        .height((heightImage.toInt()).dp)
        .width((widthImage.toInt()).dp),
        contentAlignment = Alignment.Center) {
            val painter = rememberImagePainter(
                data = url,
                builder = {

                }
            )

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.height((heightImage.toInt()).dp).width((widthImage.toInt()).dp))
    }
}
