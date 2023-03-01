package com.elka.heofficeclub.view.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.MainActivity
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.view.dialog.ErrorDialog

open class BaseFragment: Fragment() {
    protected val navController by lazy { findNavController() }
    private val activity by lazy { requireActivity() as MainActivity }

    protected val errorObserver = Observer<ErrorApp?> {
        if (it != null)  activity.errorDialog.open(getString(it.messageRes))
    }

    protected val workObserver = Observer<List<Work>> {
        if (it.isEmpty()) hideLoadingDialog() else showLoadingDialog()
    }

    protected fun showLoadingDialog() {
        activity.loadingDialog.show()
    }

    protected fun hideLoadingDialog() {
        activity.loadingDialog.dismiss()
    }
}