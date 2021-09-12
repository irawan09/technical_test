package com.electroshock.technicaltestandroid

import android.util.Log
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    init {
        Log.d("DataViewModel", "DataViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DataViewModel", "DataViewModel destroyed!")
    }
}