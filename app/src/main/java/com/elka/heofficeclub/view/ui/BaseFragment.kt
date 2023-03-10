package com.elka.heofficeclub.view.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.MainActivity
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.other.Credentials
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.view.dialog.ConfirmDialog

open class BaseFragment: Fragment() {

  protected val confirmDialog by lazy { ConfirmDialog(requireContext()) }
  protected fun restartApp() {
    try {
      val i =
        requireContext().packageManager.getLaunchIntentForPackage(requireContext().packageName)
          ?: return
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      requireActivity().finish()
      startActivity(i)
    } catch (_: Exception) {}
  }

  protected val navController by lazy { findNavController() }
  protected val activity by lazy { requireActivity() as MainActivity }

  protected val errorObserver = Observer<ErrorApp?> {
    if (it != null)  activity.informDialog.open(getString(R.string.error_title), getString(it.messageRes))
  }

  protected open val workObserver = Observer<List<Work>> {
    if (it.isEmpty()) hideLoadingDialog() else showLoadingDialog()
  }

  private fun showLoadingDialog() {
    activity.loadingDialog.show()
  }

  private fun hideLoadingDialog() {
    activity.loadingDialog.dismiss()
  }

  private val clipboard by lazy { activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
  fun copyToClipboard(data: String) {
    val label = getString(R.string.data_copied)
    val clip = ClipData.newPlainText(label, data)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(requireContext(), label, Toast.LENGTH_SHORT).show()
  }


  fun getCredentials(): Credentials? {
    val credentials = activity.sharedPreferences.getString(Constants.CREDENTIALS, null) ?: return null
    val parts = credentials.split(Constants.SEPARATOR)
    return when (parts.size) {
      2 -> Credentials(parts[0], parts[1])
      else -> null
    }
  }

  fun setCredentials(credentials: Credentials?) {
    val edit = activity.sharedPreferences.edit()
    val s = when (credentials) {
      null -> ""
      else -> "${credentials.email}${Constants.SEPARATOR}${credentials.password}"
    }
    edit.putString(Constants.CREDENTIALS, s)
    edit.apply()
  }
}