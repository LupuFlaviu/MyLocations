package com.flaviul.test.mylocations.ui.main.details.model

import androidx.lifecycle.ViewModel
import com.flaviul.test.mylocations.ui.base.model.BaseViewModelFactory

class LocationDetailsViewModelFactory : BaseViewModelFactory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = LocationDetailsViewModel() as T
}