package com.flaviul.test.mylocations.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.flaviul.test.mylocations.data.local.db.LocationDao
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.utils.Result
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.deleteFromRealm
import javax.inject.Inject


class LocalRepo @Inject constructor(private val db: Realm) : LocationDao {

    companion object {
        val TAG: String = LocalRepo::class.java.simpleName
    }

    val mResult = MutableLiveData<Result<Any>>()

    override fun saveLocations(locations: List<Location>) {
        Log.d("", "path: " + db.path)
        // Asynchronously update objects on a background thread
        mResult.value = Result.loading(true)
        db.executeTransactionAsync({
            val locationList = RealmList<Location>()
            locationList.addAll(locations)
            it.insertOrUpdate(locationList)
        }, {
            Log.d(TAG, "Locations saved successfully")
            mResult.value = Result.success(locations)
        }, {
            Log.d(TAG, "There was an error when trying to save locations: ${it.localizedMessage}")
            mResult.value = Result.failure(it)
        })
    }

    override fun saveLocation(location: Location) {
        mResult.value = Result.loading(true)
        db.executeTransactionAsync({
            location.id = it.where(Location::class.java).max("id")!!.toLong() + 1
            it.insertOrUpdate(location)
        },
            {
                Log.d(TAG, "Location ${location.label} saved successfully")
                mResult.value = Result.success(location)
            },
            {
                Log.d(TAG, "There was an error when trying to save ${location.label}: ${it.localizedMessage}")
                mResult.value = Result.failure(it)
            })
    }

    override fun updateLocation(location: Location) {
        mResult.value = Result.loading(true)
        db.executeTransactionAsync({
            it.copyToRealmOrUpdate(location)
        },
            {
                Log.d(TAG, "Location ${location.label} updated successfully")
                mResult.value = Result.success(location)
            },
            {
                Log.d(TAG, "There was an error when trying to update ${location.label}: ${it.localizedMessage}")
                mResult.value = Result.failure(it)
            })
    }

    override fun getLocations() {
        mResult.value = Result.success(db.where(Location::class.java).findAll() as List<Location>)
    }

    override fun deleteLocations() {
        mResult.value = Result.loading(true)
        db.executeTransactionAsync({ it.deleteAll() }, {
            Log.d(TAG, "Locations deleted successfully")
            mResult.value = Result.success(true)
        },
            {
                Log.d(TAG, "There was an error when trying to delete locations")
                mResult.value = Result.failure(it)
            })
    }

    override fun deleteLocation(locationId: Long) {
        mResult.value = Result.loading(true)
        db.executeTransactionAsync({
            it.where(Location::class.java).equalTo("id", locationId).findFirst()?.deleteFromRealm()
        }, {
            Log.d(TAG, "Location $locationId deleted successfully")
            mResult.value = Result.success(locationId)
        },
            {
                Log.d(TAG, "There was an error when trying to delete location $locationId")
                mResult.value = Result.failure(it)
            })
    }
}