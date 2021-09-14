package com.electroshock.technicaltestandroid.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.electroshock.technicaltestandroid.CardImageData
import com.electroshock.technicaltestandroid.api.RetrofitServiceFactory
import io.reactivex.Completable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class DataViewModel : ViewModel() {

    private val apiServiceBuilder by lazy {
        RetrofitServiceFactory.createService()
    }

    private val _data = MutableLiveData<Collection<CardImageData>>()
    val data: LiveData<Collection<CardImageData>> = _data

    var newlist = arrayListOf<CardImageData>()

    // Loading example
    enum class LoadingStatus { LOADING, NOT_LOADING }

    private val _dataLoadingStatus = MutableLiveData<LoadingStatus>()
    val dataLoadingStatus: LiveData<LoadingStatus> = _dataLoadingStatus

    private val _installLoadingStatus = MutableLiveData<LoadingStatus>()
    val installLoadingStatus: LiveData<LoadingStatus> = _installLoadingStatus

    // Completable example
    enum class InstallationStatus { SUCCESS, ERROR }

    private val _installation = MutableLiveData<InstallationStatus>()
    val installation: LiveData<InstallationStatus> = _installation

    init {
        _data.postValue(emptyList())
    }

    fun add(cardImageData: CardImageData){
        newlist.add(cardImageData)
        _data.value=newlist
        Log.d("DataViewModel", _data.value.toString())
    }

    fun getData(): ArrayList<CardImageData> {
        return newlist
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("DataViewModel", "DataViewModel destroyed!")
    }


    fun onDataRequest() = newlist.toObservable()
        .repeat(5)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .doOnSubscribe {
            _data.postValue(emptyList())
            _dataLoadingStatus.postValue(LoadingStatus.LOADING)
        }
        .doFinally { _dataLoadingStatus.postValue(LoadingStatus.NOT_LOADING) }
        .subscribeBy(
            onNext = { _data.postValue(_data.value) },
            onError = {
                _data.postValue(
                    (it.message?.let {
                        _data.value?.plus(it)
                    } ?: emptyList()) as Collection<CardImageData>?)
            },
            onComplete = { }
        )
}