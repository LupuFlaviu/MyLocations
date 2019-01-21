package com.flaviul.test.mylocations.data.api.service

import com.flaviul.test.mylocations.data.api.model.LocationResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface LocationsService {

    @GET("locations")
    fun getLocations(): Single<Response<List<LocationResponse>>>

}