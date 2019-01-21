package com.flaviul.test.mylocations.ui.main.details.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.ui.base.model.BaseViewModel

class LocationDetailsViewModel : BaseViewModel() {

    private var mSelectedLocation: Location? = null
    private var mLocations: List<Location>? = null

    private val mSelectedLocationLiveData = MutableLiveData<Location>()
    private val mMarker = MutableLiveData<Location>()
    private val mCenterMap = MutableLiveData<Location>()

    fun getSelectedLocation() = mSelectedLocationLiveData as LiveData<Location>

    fun getMarker() = mMarker as LiveData<Location>

    fun getCenterMap() = mCenterMap as LiveData<Location>

    fun onLocationSelected(location: Location?) {
        location?.let {
            this.mSelectedLocation = location
            mSelectedLocationLiveData.postValue(location)
        }
    }

    fun onLocationSelected(lat: Double, long: Double) {
        mLocations?.let {
            for (location in it) {
                if (location.latitude == lat && location.longitude == long) {
                    mSelectedLocationLiveData.postValue(location)
                    break
                }
            }
        }
    }

    fun onMapLoaded() {
        mSelectedLocation?.let {
            mMarker.postValue(it)
            mCenterMap.postValue(it)
        }
    }

    fun onLocationsReceived(locations: List<Location>) {
        this.mLocations = locations
        for (location in locations) {
            mMarker.value = location
        }
    }
}