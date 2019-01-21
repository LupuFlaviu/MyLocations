package com.flaviul.test.mylocations.data.repository

import androidx.lifecycle.LiveData
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.utils.NetworkUtils
import com.flaviul.test.mylocations.utils.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import retrofit2.Response
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val localRepo: LocalRepo,
    private val remoteRepo: RemoteRepo,
    private val networkUtils: NetworkUtils
) {

    private val mLocationsResult = localRepo.mResult
    private var mShouldGetRemoteLocations = true

    fun getLocationsObservable(): LiveData<Result<Any>> = mLocationsResult

    fun getRemoteLocations(): Disposable {
        if (mShouldGetRemoteLocations) {
            mLocationsResult.value = Result.loading(true)
            return remoteRepo.getLocations()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleServerResponse, this::handleServerError)
        }
        return Disposables.empty()
    }

    fun getLocalLocations() {
        mLocationsResult.value = Result.loading(true)
        localRepo.getLocations()
    }

    private fun handleServerResponse(response: Response<List<com.flaviul.test.mylocations.data.api.model.LocationResponse>>) =
        if (response.isSuccessful) {
            val localLocationList = response.body()!!.map { Location(it.id, it.lat, it.long, it.label, it.address) }
            localRepo.saveLocations(localLocationList)
            mLocationsResult.value = Result.success(localLocationList)
            mShouldGetRemoteLocations = false
        } else {

            mLocationsResult.value = Result.failure(Throwable(response.errorBody().toString()))
        }

    private fun handleServerError(throwable: Throwable) {
        if (networkUtils.isNetworkUnavailable()) {
            mLocationsResult.value = Result.failure(Throwable("No Internet Connection"))
        } else {
            mLocationsResult.value = Result.failure(throwable)
        }
    }

    fun addLocation(location: Location) {
        localRepo.saveLocation(location)
    }

    fun updateLocation(location: Location) {
        localRepo.updateLocation(location)
    }

    fun deleteLocation(locationId: Long) {
        localRepo.deleteLocation(locationId)
    }
}