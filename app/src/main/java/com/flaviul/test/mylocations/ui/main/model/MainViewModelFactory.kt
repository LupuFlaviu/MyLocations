package com.flaviul.test.mylocations.ui.main.model

import androidx.lifecycle.ViewModel
import com.flaviul.test.mylocations.data.repository.LocationsRepository
import com.flaviul.test.mylocations.ui.base.model.BaseViewModelFactory
import javax.inject.Inject

class MainViewModelFactory @Inject constructor(private val locationsRepository: LocationsRepository) :
    BaseViewModelFactory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) = MainViewModel(
        locationsRepository
    ) as T
}