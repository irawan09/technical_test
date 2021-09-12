package com.electroshock.technicaltestandroid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    var listData = MutableLiveData<ArrayList<CardImageData>>()
    var newlist = arrayListOf<CardImageData>()

    init {
        Log.d("DataViewModel", "DataViewModel created!")
    }

    fun add(cardImageData: CardImageData){
        newlist.add(cardImageData)
        listData.value=newlist
    }

    fun remove(cardImageData: CardImageData){
        newlist.remove(cardImageData)
        listData.value=newlist
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DataViewModel", "DataViewModel destroyed!")
    }
}