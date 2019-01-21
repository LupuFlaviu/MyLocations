package com.flaviul.test.mylocations.ui.main.list.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flaviul.test.mylocations.ui.base.model.BaseViewModel

class LocationsListViewModel :
    BaseViewModel() {

    private val mCoordinates = MutableLiveData<Pair<Double, Double>>()

    private var mLat: Double? = null
    private var mLong: Double? = null

    fun getCoordinates() = mCoordinates as LiveData<Pair<Double, Double>>

    fun onLocationChanged(latitude: Double, longitude: Double) =
        if (this.mLat != latitude || this.mLong != longitude) {
            this.mLat = latitude
            this.mLong = longitude
            mCoordinates.postValue(Pair(latitude, longitude))
        } else Unit
}