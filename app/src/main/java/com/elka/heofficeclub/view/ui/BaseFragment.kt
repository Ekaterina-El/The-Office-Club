package com.elka.heofficeclub.view.ui

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.MainActivity
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.view.dialog.ErrorDialog

open class BaseFragment: Fragment() {
  protected fun restartApp() {
    try {
      val i =
        requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
          ?: return
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      requireActivity().finish()
      startActivity(i)
    } catch (e: Exception) {}
  }

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