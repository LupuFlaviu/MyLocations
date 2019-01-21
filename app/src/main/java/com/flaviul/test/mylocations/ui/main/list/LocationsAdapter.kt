package com.flaviul.test.mylocations.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flaviul.test.mylocations.R
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.utils.calculateDistance
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_location.*

class LocationsAdapter(
    var locations: List<Location>,
    var isLocationAvailable: Boolean = false,
    private val onLocationClicked: (Location) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {

    private var mLat: Double? = null
    private var mLong: Double? = null

    fun setCoordinates(lat: Double, long: Double) =
        if (mLat == null || mLat != lat) {
            mLat = lat
            mLong = long
            notifyDataSetChanged()
        } else Unit

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = locations.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(locations[position], onLocationClicked)
    }

    inner class ViewHolder(override val containerView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindData(location: Location, onLocationClicked: (Location) -> Unit) {
            labelText.text = location.label
            if (mLat == null || mLong == null) {
                if (isLocationAvailable) {
                    distanceText.text =
                            containerView.context.getString(R.string.calculating)
                } else {
                    distanceText.text =
                            containerView.context.getString(R.string.not_available)
                }
            } else {
                val distance = calculateDistance(
                    mLat!!, mLong!!,
                    location.latitude, location.longitude
                )
                val formattedDistance =
                    String.format(containerView.context.getString(R.string.distance_in_km), distance)
                distanceText.text = formattedDistance
            }
            itemContainer.setOnClickListener {
                onLocationClicked(location)
            }
        }
    }
}