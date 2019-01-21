package com.flaviul.test.mylocations.data.repository

import com.flaviul.test.mylocations.data.api.model.LocationResponse
import com.flaviul.test.mylocations.data.api.service.LocationsService
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class RemoteRepo @Inject constructor(private val locationsService: LocationsService) {

    fun getLocations(): Single<Response<List<LocationResponse>>> = locationsService.getLocations()
}