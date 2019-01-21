package com.flaviul.test.mylocations.ui.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.flaviul.test.mylocations.ui.base.model.BaseViewModel
import com.flaviul.test.mylocations.ui.base.model.BaseViewModelFactory
import com.flaviul.test.mylocations.utils.hideKeyboard
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel, VMF : BaseViewModelFactory> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: VMF

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
    }

    protected abstract fun getViewModelClass(): Class<VM>

    fun replaceFragment(fragment: androidx.fragment.app.Fragment, container: Int, addToBackStack: Boolean) {
        hideKeyboard()
        val currentFragment = supportFragmentManager.findFragmentById(container)
        if (fragment.javaClass.isInstance(currentFragment)) {
            return
        }
        val transaction: androidx.fragment.app.FragmentTransaction = if (addToBackStack) {
            supportFragmentManager
                .beginTransaction()
                .replace(container, fragment)
                .addToBackStack(fragment.javaClass.name)
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(container, fragment)
        }
        transaction.commit()
    }

    protected fun clearBackStack() {
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
    }
}