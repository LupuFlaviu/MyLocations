package com.flaviul.test.mylocations.ui.main.model

import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.data.repository.LocationsRepository
import com.flaviul.test.mylocations.ui.base.model.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val locationsRepository: LocationsRepository) : BaseViewModel() {

    var mSelectedLocation: Location? = null

    fun getLocations() = locationsRepository.getLocationsObservable()

    fun getRemoteLocations() {
        addDisposable(locationsRepository.getRemoteLocations())
    }

    fun getLocalLocations() = locationsRepository.getLocalLocations()

    fun addLocation(location: Location) =
        locationsRepository.addLocation(location)

    fun updateLocation(location: Location) = locationsRepository.updateLocation(location)

    fun onSelectedLocation(location: Location) {
        this.mSelectedLocation = location
    }

    fun deleteLocation() {
        mSelectedLocation?.let {
            locationsRepository.deleteLocation(it.id)
        }
    }
}