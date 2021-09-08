package com.electroshock.technicaltestandroid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.tooling.preview.Preview
import com.electroshock.technicaltestandroid.ui.theme.TechnicalTestAndroidTheme
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {

    val dataPojo = Data()
    var itemsArray1: ArrayList<Cell> = ArrayList()
    var itemsArray2: ArrayList<Cell> = ArrayList()
    var itemsArray3: ArrayList<List<Cell>> = ArrayList()

//    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechnicalTestAndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    if (isNetworkAvailable(applicationContext) == true){
                        MessageCard(Cell("Android", "Jetpack Compose"))

//                        parseJSON()
                    }else {
                        Toast.makeText(this@MainActivity, "Network Not Available", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
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

//        var headerText = findViewById<TextView>(R.id.header_title)
//        var subHeaderText = findViewById<TextView>(R.id.subheader_title)
//        var descSubHeaderText = findViewById<TextView>(R.id.descsubheader_title)

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

                                    Log.d("Array 3 saya ", itemsArray3.toString())

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

                                    val modelCard =
                                        Cell(
                                            cardTitle,
                                            cardDescription,
                                            cardImage,
                                            cardTitleTextColor,
                                            cardTitleTextSize,
                                            cardImageWidth,
                                            cardImageHeight
                                        )
                                    itemsArray1.add(modelCard)
//                                    Log.d("Array 1 saya ", itemsArray1.toString())
                                    itemsArray3.add(itemsArray1)
                                    Log.d("Array 3 saya ", itemsArray3.toString())

                                } else {
                                    Log.d("Array 3 saya ", itemsArray3.toString())

                                    // Card Title
                                    val cardTitle = items[i].card?.cardTitle?.titleValue ?: "N/A"
                                    // Card Description
                                    val cardDescription = items[i].card?.cardDescription?.descriptionValue
                                        ?: "N/A"
                                    // Card Image
                                    val cardImage = items[i].card?.cardImage?.imageUrl ?: "N/A"
                                    //Card Title/Description text color
                                    val cardTitleTextColor = items[i].card?.cardAttributes?.attributesTextColor
                                        ?: "N/A"
                                    //Card Title/Description Text Size
                                    val cardTitleTextSize = items[i].card?.cardAttributes?.attributesFont?.attributesFontSize
                                        ?: "N/A"
                                    //Card Image width
                                    val cardImageWidth = items[i].card?.cardImage?.imageSize?.imageSizeWidth
                                        ?: "N/A"
                                    //Card Image height
                                    val cardImageHeight = items[i].card?.cardImage?.imageSize?.imageSizeHeight
                                        ?: "N/A"

                                    val modelCard =
                                        Cell(
                                            cardTitle,
                                            cardDescription,
                                            cardImage,
                                            cardTitleTextColor,
                                            cardTitleTextSize,
                                            cardImageWidth,
                                            cardImageHeight
                                        )
                                    itemsArray2.add(modelCard)
                                    Log.d("Array 3 saya ", itemsArray3.toString())
                                    itemsArray3.add(itemsArray2)

//                                    adapter = RecycleViewAdapter(itemsArray2)
//                                    adapter.notifyDataSetChanged()
                                }

                            }
                        }
                    }

//                    binding.jsonResultsRecyclerview.adapter = adapter

//                    //Setting header Home App text
//                    headerText.setText(dataPojo.headerValue)
//                    headerText.setTextColor(Color.parseColor(dataPojo.headerTextColor))
//                    var sizeTextJson = dataPojo.headerFontSize
//                    headerText.setTextSize(sizeTextJson.toFloat())
//
//                    //Setting subheader Home App text
//                    subHeaderText.setText(dataPojo.subHeaderValue)
//                    subHeaderText.setTextColor(Color.parseColor(dataPojo.subHeaderTextColor))
//                    var subSizeTextJson = dataPojo.subHeaderFontSize
//                    subHeaderText.setTextSize(subSizeTextJson.toFloat())
//
//                    //Setting subheader description Home App text
//                    descSubHeaderText.setText(dataPojo.descSubHeaderValue)
//                    descSubHeaderText.setTextColor(Color.parseColor(dataPojo.descSubHeaderTextColor))
//                    var descSubSizeTextJson = dataPojo.descSubHeaderFontSize
//                    descSubHeaderText.setTextSize(descSubSizeTextJson.toFloat())

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
    companion object {
        var baseUrl = "https://private-8ce77c-tmobiletest.apiary-mock.com/"
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun MessageCard(cell: Cell){
    Text(cell.cardTitle)
    Text(cell.cardDescription)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TechnicalTestAndroidTheme {
        MessageCard(
            cell = Cell("Colleague", "Hey, take a look at Jetpack Compose, it's great!")
        )
    }
}