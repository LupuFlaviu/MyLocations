package com.flaviul.test.mylocations.ui.main

import androidx.annotation.StringRes
import com.flaviul.test.mylocations.data.local.db.model.Location

interface MainController {

    interface Navigator {

        fun navigateToLocationsList()

        fun navigateToLocationDetails(location: Location)
    }

    interface View {

        fun setToolbarTitle(@StringRes titleId: Int)

        fun showBackButton()

        fun hideBackButton()

        fun showAddButton()

        fun hideAddButton()

        fun showEditButton()

        fun hideEditButton()

        fun showDeleteButton()

        fun hideDeleteButton()
    }
}