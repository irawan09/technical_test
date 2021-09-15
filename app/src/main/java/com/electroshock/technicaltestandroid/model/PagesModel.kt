package com.electroshock.technicaltestandroid

import com.google.gson.annotations.SerializedName

data class PagesModel(

    @SerializedName("page")
    val pages: Page?

)

data class Page(

    @SerializedName("cards")
    var cards: ArrayList<Cards>?
)

data class Cards(

    @SerializedName("card_type")
    val card_type: String?,

    @SerializedName("card")
    val card: Card?
)

data class Card(

    @SerializedName("title")
    val cardTitle: CardTitle?,

    @SerializedName("description")
    val cardDescription: CardDescription?,

    @SerializedName("image")
    val cardImage: CardImage?,

    @SerializedName("value")
    val cardValue: String?,

    @SerializedName("attributes")
    val cardAttributes: CardAttributes?

)

data class CardAttributes(

    @SerializedName("text_color")
    val attributesTextColor : String?,

    @SerializedName("font")
    val attributesFont: AttributesFont?
)

data class CardTitle(

    @SerializedName("value")
    val titleValue: String?,

    @SerializedName("attributes")
    val titleAttributes: TitleAttributes?
)

data class CardDescription(

    @SerializedName("value")
    val descriptionValue: String?,

    @SerializedName("attributes")
    val descriptionAttributes: DescriptionAttributes?
)
data class CardImage(

    @SerializedName("url")
    val imageUrl: String?,

    @SerializedName("size")
    val imageSize: ImageSize?
)


data class AttributesFont(

    @SerializedName("size")
    val attributesFontSize : String?
)

data class TitleAttributes(
    @SerializedName("text_color")
    val titleAttributesTextColor: String?,

    @SerializedName("font")
    val titleAttributesSize : TitleAttributesSize?
)

data class TitleAttributesSize(
    @SerializedName("size")
    val size : String?
)

data class DescriptionAttributes(
    @SerializedName("text_color")
    val descriptionAttributesTextColor: String?,

    @SerializedName("font")
    val descriptionAttributesFontSize : DescriptionAttributesFontSize?
)

data class DescriptionAttributesFontSize(
    @SerializedName("size")
    val size : String?
)

data class ImageSize(
    @SerializedName("width")
    val imageSizeWidth: String?,

    @SerializedName("height")
    val imageSizeHeight: String?
)
