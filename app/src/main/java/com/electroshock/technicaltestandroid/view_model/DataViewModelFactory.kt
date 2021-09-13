package com.electroshock.technicaltestandroid.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DataViewModelFactory():ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DataViewModel::class.java)){
            return DataViewModel() as T
        }
        throw IllegalArgumentException ("UnknownViewModel")
    }
}