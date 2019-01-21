package com.flaviul.test.mylocations.utils

import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun AppCompatActivity.hideKeyboard() =
    run { (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager) }
        .hideSoftInputFromWindow(currentFocus?.windowToken, 0)

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
    observe(lifecycleOwner, Observer { it?.let(action) })

fun LocationManager.requestLocationUpdates(
    provider: String, minTime: Long, minDistance: Float,
    onLocationChanged: (location: android.location.Location?) -> Unit
) {
    try {
        requestLocationUpdates(provider, minTime, minDistance, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                onLocationChanged(location)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
        })
    } catch (ex: SecurityException) {
        ex.printStackTrace()
    }
}

fun EditText.addTextChangedListener(onTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(text.toString())
        }
    })
}