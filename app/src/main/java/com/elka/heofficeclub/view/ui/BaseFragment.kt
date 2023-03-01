package com.elka.heofficeclub.view.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.MainActivity
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.view.dialog.ErrorDialog

open class BaseFragment: Fragment() {
    protected val navController by lazy { findNavController() }

    protected val errorObserver = Observer<ErrorApp?> {
        if (it != null)  (requireActivity() as MainActivity).errorDialog.open(getString(it.messageRes))
    }
}