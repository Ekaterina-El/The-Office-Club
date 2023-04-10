package com.elka.heofficeclub.view.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.MainActivity
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.view.dialog.ConfirmDialog
import com.google.android.material.textfield.TextInputLayout
import java.util.HashMap

open class BaseFragment : Fragment() {

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
    } catch (_: Exception) {
    }
  }

  protected val navController by lazy { findNavController() }
  protected val activity by lazy { requireActivity() as MainActivity }

  val errorObserver = Observer<ErrorApp?> {
    if (it != null) activity.informDialog.open(
      getString(R.string.error_title),
      getString(it.messageRes)
    )
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
    val credentials =
      activity.sharedPreferences.getString(Constants.CREDENTIALS, null) ?: return null
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

  val permissionWrite = WRITE_EXTERNAL_STORAGE
  val permissionRead = READ_EXTERNAL_STORAGE

  val createDocumentPermissionGranted: Boolean
    get() =
      ContextCompat
        .checkSelfPermission(requireContext(), permissionWrite) == PackageManager.PERMISSION_GRANTED
          && ContextCompat
        .checkSelfPermission(requireContext(), permissionRead) == PackageManager.PERMISSION_GRANTED

  val createDocumentPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
      for (permission in permissions) {
        if (permission.key == permissionWrite || permission.key == permissionRead) {
          when (permission.value) {
            true -> Unit
            false -> {
              val title = getString(R.string.permission_deny)
              val message = getString(R.string.permission_create_documeny_message)
              activity.informDialog.open(title, message, onButtonListener = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
              })
            }
          }
        }
      }
    }

  fun showErrors(errors: List<FieldError>?, fields: HashMap<Field, Any>) {
    for (field in fields) {
      val error = errors?.firstOrNull { it.field == field.key }
      val errorStr = error?.let { getString(it.errorType!!.messageRes) } ?: ""

      when (val f = field.value) {
        is TextInputLayout -> f.error = errorStr
        is TextView -> f.text = errorStr
        else -> Unit
      }
    }
  }
}