package com.flaviul.test.mylocations.utils.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Window
import android.widget.TextView
import com.flaviul.test.mylocations.R
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.utils.addTextChangedListener
import kotlinx.android.synthetic.main.location_dialog.*
import kotlinx.android.synthetic.main.fragment_location_details.*


class LocationDialog(
    private var activity: Activity,
    private val mode: Mode,
    private val selectedLocation: Location?,
    private val onPositiveClick: (Location) -> Unit
) : Dialog(activity) {

    enum class Mode {
        ADD_MODE,
        EDIT_MODE;
    }

    private var positiveButton: TextView? = null
    private var negativeButton: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.location_dialog)

        if (mode == Mode.EDIT_MODE) {
            titleText.text = activity.getString(R.string.edit_location)
            latitudeEditText.text = SpannableStringBuilder(activity.latitudeText.text)
            longitudeEditText.text = SpannableStringBuilder(activity.longitudeText.text)
            labelEditText.text = SpannableStringBuilder(activity.labelText.text)
            addressEditText.text = SpannableStringBuilder(activity.addressText.text)
        }

        positiveButton = findViewById(R.id.okButton)
        negativeButton = findViewById(R.id.cancelButton)

        addTextListeners()

        positiveButton?.setOnClickListener {
            val location = Location(
                latitude = latitudeEditText.text.toString().toDouble(),
                longitude = longitudeEditText.text.toString().toDouble(),
                tag = labelEditText.text.toString(),
                address = addressEditText.text.toString()
            )
            if (mode == Mode.EDIT_MODE) {
                location.id = selectedLocation!!.id
            }
            onPositiveClick(location)
            dismiss()
        }

        negativeButton?.setOnClickListener {
            dismiss()
        }

    }

    private fun addTextListeners() {
        latitudeEditText.addTextChangedListener {
            enablePositiveButton()
        }
        longitudeEditText.addTextChangedListener {
            enablePositiveButton()
        }
        labelEditText.addTextChangedListener {
            enablePositiveButton()
        }
        addressEditText.addTextChangedListener {
            enablePositiveButton()
        }
    }

    private fun enablePositiveButton() {
        okButton.isEnabled = latitudeEditText.text.toString().isNotEmpty() &&
                longitudeEditText.text.toString().isNotEmpty() &&
                labelEditText.text.toString().isNotEmpty() &&
                addressEditText.text.toString().isNotEmpty()
    }
}