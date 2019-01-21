package com.flaviul.test.mylocations.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.flaviul.test.mylocations.R
import com.flaviul.test.mylocations.data.local.db.model.Location
import com.flaviul.test.mylocations.ui.base.BaseActivity
import com.flaviul.test.mylocations.ui.main.details.LocationDetailsFragment
import com.flaviul.test.mylocations.ui.main.list.LocationsListFragment
import com.flaviul.test.mylocations.ui.main.model.MainViewModel
import com.flaviul.test.mylocations.ui.main.model.MainViewModelFactory
import com.flaviul.test.mylocations.utils.ui.LocationDialog
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity<MainViewModel, MainViewModelFactory>(), MainController.Navigator,
    MainController.View {

    override fun getViewModelClass() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.backStackEntryCount == 0) {
            navigateToLocationsList()
        }

        onMenuOptionsClick()
    }

    private fun onMenuOptionsClick() {
        onAddClick()
        onEditClick()
        onDeleteClick()
        onBackClick()
    }

    private fun onAddClick() {
        addButton.setOnClickListener {
            val dialog = LocationDialog(this, LocationDialog.Mode.ADD_MODE, null) {
                viewModel.addLocation(it)
            }
            dialog.show()
        }
    }

    private fun onEditClick() {
        editButton.setOnClickListener {
            val dialog =
                LocationDialog(this, LocationDialog.Mode.EDIT_MODE, viewModel.mSelectedLocation) {
                    viewModel.updateLocation(it)
                }
            dialog.show()
        }
    }

    private fun onDeleteClick() {
        deleteButton.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_location)
            .setPositiveButton(R.string.confirm) { _, _ ->
                viewModel.deleteLocation()
                onBackPressed()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun onBackClick() {
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }

    override fun navigateToLocationsList() {
        clearBackStack()
        replaceFragment(LocationsListFragment(), R.id.mainContainer, true)
    }

    override fun navigateToLocationDetails(location: Location) =
        replaceFragment(LocationDetailsFragment.createFragment(location), R.id.mainContainer, true)

    override fun setToolbarTitle(titleId: Int) {
        titleText.text = getString(titleId)
    }

    override fun showBackButton() {
        backButton.visibility = View.VISIBLE
    }

    override fun hideBackButton() {
        backButton.visibility = View.GONE
    }

    override fun showAddButton() {
        addButton.visibility = View.VISIBLE
    }

    override fun hideAddButton() {
        addButton.visibility = View.GONE
    }

    override fun showEditButton() {
        editButton.visibility = View.VISIBLE
    }

    override fun hideEditButton() {
        editButton.visibility = View.GONE
    }

    override fun showDeleteButton() {
        deleteButton.visibility = View.VISIBLE
    }

    override fun hideDeleteButton() {
        deleteButton.visibility = View.GONE
    }
}
