package com.flaviul.test.mylocations.data.local.db

import com.flaviul.test.mylocations.data.local.db.model.Location

interface LocationDao {

    fun saveLocations(locations: List<Location>)
    fun saveLocation(location: Location)
    fun updateLocation(location: Location)
    fun getLocations()
    fun deleteLocations()
    fun deleteLocation(locationId: Long)
}