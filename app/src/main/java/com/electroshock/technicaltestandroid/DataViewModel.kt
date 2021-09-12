package com.electroshock.technicaltestandroid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    var cardImage: String = "N/A"
    var cardImageWidth: String = "N/A"
    var cardImageHeight: String = "N/A"
    var cardImageTitle: String = "N/A"
    var cardImageTitleTextColor: String = "N/A"
    var cardImageTitleTextSize: String = "N/A"
    var cardImageDescription: String = "N/A"
    var cardImageDescriptionTextColor: String = "N/A"
    var cardImageDescriptionTextSize: String = "N/A"

    var dataModel  = MutableLiveData<CardImageData>()


    init {
        Log.d("DataViewModel", "DataViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DataViewModel", "DataViewModel destroyed!")
    }
}