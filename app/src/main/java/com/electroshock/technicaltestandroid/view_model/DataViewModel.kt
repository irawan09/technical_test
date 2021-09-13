package com.electroshock.technicaltestandroid.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.electroshock.technicaltestandroid.CardImageData

class DataViewModel : ViewModel() {

    var listData = MutableLiveData<ArrayList<CardImageData>>()
    var newlist = arrayListOf<CardImageData>()

    init {
        Log.d("DataViewModel", "DataViewModel created!")
    }

    fun add(cardImageData: CardImageData){
        newlist.add(cardImageData)
        listData.value=newlist
        Log.d("DataViewModel", (listData.value).toString())
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DataViewModel", "DataViewModel destroyed!")
    }
}