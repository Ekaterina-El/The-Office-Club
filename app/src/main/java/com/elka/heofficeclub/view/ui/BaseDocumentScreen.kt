package com.elka.heofficeclub.view.ui

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.other.toDocFormat
import com.elka.heofficeclub.other.toDownloadDocFormat
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel
import java.util.*

abstract class BaseDocumentScreen : BaseFragmentWithOrganization() {
  protected val documentsViewModel by activityViewModels<OrganizationDocsViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          goBack()
        }
      })
  }

  fun goBack() {
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }

  fun downloadFileByUrl(fileUrl: String, date: Date, type: FormType, arg: String? = null) {
    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }


    val datePart = date.toDownloadDocFormat()
    val namePart  = when(type) {
      FormType.T1 -> "Т_1"
      FormType.T2 -> "Т_2"
      FormType.T3 -> "Т_3"
      FormType.T5 -> "Т_5"
      FormType.T6 -> "Т_6"
      FormType.T7 -> "Т_7"
      FormType.T8 -> "Т_8"
      FormType.T11 -> "Т_11"
    }
    val docName = "${namePart}_$datePart.pdf"

    val request = DownloadManager.Request(Uri.parse(fileUrl)).setMimeType("application/pdf")
      .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
      .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
      .setTitle(docName)
      .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TheOfficeClub/$docName")
    request.allowScanningByMediaScanner()

    val fileId = manager.enqueue(request)
    manager.getUriForDownloadedFile(fileId)
//    manager.openDownloadedFile(fileId)
  }

  private val manager by lazy {
    requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
  }

  abstract fun download()
}