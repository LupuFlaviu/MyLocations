package com.flaviul.test.mylocations.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.flaviul.test.mylocations.R
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.ui.base.BaseFragment
import com.flaviul.test.mylocations.ui.main.MainController
import com.flaviul.test.mylocations.ui.main.model.MainViewModel
import com.flaviul.test.mylocations.ui.main.details.model.LocationDetailsViewModel
import com.flaviul.test.mylocations.ui.main.details.model.LocationDetailsViewModelFactory
import com.flaviul.test.mylocations.utils.Result
import com.flaviul.test.mylocations.utils.observeNonNull
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_location_details.*
import javax.inject.Inject


class LocationDetailsFragment : BaseFragment<LocationDetailsViewModel, LocationDetailsViewModelFactory>(),
    OnMapReadyCallback {

    @Inject
    lateinit var mController: MainController.View

    private var mMap: GoogleMap? = null

    override fun getViewModelClass() = LocationDetailsViewModel::class.java

    companion object {
        private const val LOCATION_PARAMETER_KEY = "LOCATION_PARAMETER_KEY"
        fun createFragment(location: Location) = LocationDetailsFragment().apply {
            arguments = Bundle().apply { putParcelable(LOCATION_PARAMETER_KEY, location) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_location_details, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val location = arguments?.getParcelable<Location>(LOCATION_PARAMETER_KEY)
        viewModel.onLocationSelected(location)

        observeSelectedLocation()
        observeMarker()
        observeCenterMap()
    }

    private fun observeLocations() {
        ViewModelProviders.of(activity!!).get(MainViewModel::class.java).getLocations().observeNonNull(this) {
            when (it) {
                is Result.Success -> if (it.data is List<*> && !(it.data as List<*>).isNullOrEmpty()) {
                    viewModel.onLocationsReceived(it.data as List<Location>)
                } else if (it.data is Location) {
                    labelText.text = (it.data as Location).label
                    addressText.text = (it.data as Location).address
                    latitudeText.text = (it.data as Location).latitude.toString()
                    longitudeText.text = (it.data as Location).longitude.toString()
                }
            }
        }
    }

    private fun observeSelectedLocation() {
        viewModel.getSelectedLocation().observeNonNull(this) { location ->
            labelText.text = location.label
            latitudeText.text = String.format("%.7f", location.latitude)
            longitudeText.text = String.format("%.7f", location.longitude)
            addressText.text = location.address
            ViewModelProviders.of(activity!!).get(MainViewModel::class.java).onSelectedLocation(location)
        }
    }

    private fun observeMarker() {
        viewModel.getMarker().observeNonNull(this) { location ->
            addMarker(location)
        }
    }

    private fun observeCenterMap() {
        viewModel.getCenterMap().observeNonNull(this) { location ->
            centerMap(location)
        }
    }

    override fun onResume() {
        super.onResume()
        mController.setToolbarTitle(R.string.location_details_title)
        mController.hideAddButton()
        mController.showDeleteButton()
        mController.showEditButton()
        mController.showBackButton()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap
        this.mMap?.uiSettings?.isMapToolbarEnabled = false
        this.mMap?.setOnMarkerClickListener { marker ->
            viewModel.onLocationSelected(marker.position.latitude, marker.position.longitude)
            false
        }
        observeLocations()
        viewModel.onMapLoaded()
    }

    private fun addMarker(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(coordinates).title(location.label).snippet(location.address)
        mMap?.addMarker(markerOptions)
    }

    private fun centerMap(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        val cameraPosition = CameraPosition.Builder().target(coordinates).zoom(16f).build()
        mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
}