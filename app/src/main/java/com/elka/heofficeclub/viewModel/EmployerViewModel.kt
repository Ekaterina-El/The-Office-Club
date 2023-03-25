package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log

class EmployerViewModel(application: Application) : BaseViewModelEmployer(application) {
    override val screens: Int = 6

    override fun onEndScreens() {
        Log.d("onEndScreens", "end")
    }

}