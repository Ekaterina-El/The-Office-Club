package com.elka.heofficeclub.view.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

open class BaseFragment: Fragment() {
    protected val navController by lazy { findNavController() }
}