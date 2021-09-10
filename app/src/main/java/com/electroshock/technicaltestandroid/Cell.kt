package com.electroshock.technicaltestandroid

import androidx.compose.ui.graphics.Color

data class Cell(
    var cardTitle: String  = "N/A",
    var cardTextColor: String = "N/A",
    var cardTextSize: String = "N/A",
    var cardDescription: String  = "N/A",
)

data class ImageData(
    var cardImage: String = "N/A",
    var cardImageWidth: String = "N/A",
    var cardImageHeight: String = "N/A",
    var cardImageTitle: String = "N/A",
    var cardImageTitleTextColor: String = "N/A",
    var cardImageTitleTextSize: String = "N/A",
    var cardImageDescription: String = "N/A",
    var cardImageDescriptionTextColor: String = "N/A",
    var cardImageDescriptionTextSize: String = "N/A"

)

data class TitleImageData(
    var cardTitle: String  = "N/A",
    var cardTextColor: String = "N/A",
    var cardTextSize: String = "N/A",
    var cardDescription: String  = "N/A",
    var cardDescriptionTextColor: String = "N/A",
    var cardDescriptionTextSize: String = "N/A",
)
