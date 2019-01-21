package com.flaviul.test.mylocations.ui.main.list.model

import androidx.lifecycle.ViewModel
import com.flaviul.test.mylocations.ui.base.model.BaseViewModelFactory

class LocationsListViewModelFactory : BaseViewModelFactory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = LocationsListViewModel() as T
}