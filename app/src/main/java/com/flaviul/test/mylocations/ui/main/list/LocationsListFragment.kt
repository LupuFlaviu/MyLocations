package com.flaviul.test.mylocations.ui.main.list

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.flaviul.test.mylocations.BuildConfig
import com.flaviul.test.mylocations.R
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.ui.base.BaseFragment
import com.flaviul.test.mylocations.ui.main.MainController
import com.flaviul.test.mylocations.ui.main.list.model.LocationsListViewModel
import com.flaviul.test.mylocations.ui.main.list.model.LocationsListViewModelFactory
import com.flaviul.test.mylocations.ui.main.model.MainViewModel
import com.flaviul.test.mylocations.utils.*
import kotlinx.android.synthetic.main.fragment_locations_list.*
import javax.inject.Inject


class LocationsListFragment : BaseFragment<LocationsListViewModel, LocationsListViewModelFactory>() {

    @Inject
    lateinit var mViewController: MainController.View

    @Inject
    lateinit var mNavController: MainController.Navigator

    private lateinit var mLocationAdapter: LocationsAdapter

    private val onLocationChanged: (android.location.Location?) -> Unit = { location ->
        location?.let {
            Log.i(
                LocationsListFragment::class.java.simpleName,
                "Current location is ${it.latitude} - ${it.longitude}"
            )
            if (::mLocationAdapter.isInitialized) {
                viewModel.onLocationChanged(it.latitude, it.longitude)
            }
        }
    }

    companion object {
        private const val REFRESH_TIME = 1000L
        private const val REFRESH_DISTANCE = 1f
    }

    override fun getViewModelClass() = LocationsListViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_locations_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationsRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )
        val decoration = DividerItemDecoration(context, VERTICAL)
        locationsRecyclerView.addItemDecoration(decoration)
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java).getRemoteLocations()
        observeLocations()
        observeCoordinates()

        if (isLocationPermissionGranted(view.context)) {
            onLocationPermissionGranted()
        } else {
            requestLocationPermission()
        }
    }

    private fun observeLocations() {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java).getLocations().observeNonNull(this) {
            when (it) {
                is Result.Success -> if (it.data is List<*>) {
                    if (!(it.data as List<*>).isNullOrEmpty()) {
                        showLocations(it.data as List<Location>)
                    }
                } else if (it.data is Location || it.data is Long) {
                    ViewModelProviders.of(activity!!).get(MainViewModel::class.java).getLocalLocations()
                }
            }
        }
    }

    private fun observeCoordinates() {
        viewModel.getCoordinates().observeNonNull(this) {
            mLocationAdapter.setCoordinates(it.first, it.second)
        }
    }

    private fun requestLocationPermission() =
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            Constants.REQUEST_CODE_ACCESS_LOCATION
        )

    private fun showLocations(locations: List<Location>) {
        if (!::mLocationAdapter.isInitialized) {
            val isLocationEnabled =
                context != null && isLocationPermissionGranted(context!!) && isLocationEnabled(context!!)
            mLocationAdapter = LocationsAdapter(locations, isLocationEnabled) { location ->
                mNavController.navigateToLocationDetails(location)
            }
        } else {
            mLocationAdapter.locations = locations
            mLocationAdapter.notifyDataSetChanged()
        }
        locationsRecyclerView.adapter = mLocationAdapter
    }

    override fun onResume() {
        super.onResume()
        mViewController.setToolbarTitle(R.string.location_list_title)
        mViewController.showAddButton()
        mViewController.hideBackButton()
        mViewController.hideEditButton()
        mViewController.hideDeleteButton()

        if (context != null && isLocationPermissionGranted(context!!) && isLocationEnabled(context!!)) {
            onLocationEnabled()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_ACCESS_LOCATION && !grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onLocationPermissionGranted()
        } else if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) && context != null) {
            AlertDialog.Builder(context!!)
                .setMessage(R.string.request_location)
                .setPositiveButton(R.string.allow_permission) { _, _ ->
                    startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                        )
                    )
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun onLocationPermissionGranted() {
        if (activity != null && context != null && !isLocationEnabled(context!!)) {
            requestLocationAccess(activity!!)
        } else {
            onLocationEnabled()
        }
    }

    private fun onLocationEnabled() {
        val locationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, REFRESH_TIME,
            REFRESH_DISTANCE, onLocationChanged
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER, REFRESH_TIME,
            REFRESH_DISTANCE, onLocationChanged
        )
        if (::mLocationAdapter.isInitialized) {
            mLocationAdapter.isLocationAvailable = true
            mLocationAdapter.notifyDataSetChanged()
        }
    }

}