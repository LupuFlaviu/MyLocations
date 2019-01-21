package com.flaviul.test.mylocations.utils

import android.net.ConnectivityManager
import javax.inject.Inject

class NetworkUtils @Inject constructor(private val connectivityManager: ConnectivityManager) {

    fun isNetworkUnavailable(): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork == null || !activeNetwork.isConnected
    }
}
