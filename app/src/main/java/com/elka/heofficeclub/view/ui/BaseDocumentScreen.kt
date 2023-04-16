package com.elka.heofficeclub.view.ui

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

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

  fun downloadFileByUrl(fileUrl: String, fileName: String) {
    if (!createDocumentPermissionGranted) {
      createDocumentPermissionLauncher.launch(arrayOf(permissionRead, permissionWrite))
      return
    }

    val docName = "$fileName.pdf"
    val request = DownloadManager.Request(Uri.parse(fileUrl)).setMimeType("application/pdf")
      .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
      .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
      .setTitle(docName)
      .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "TheOfficeClub/$docName")
    request.allowScanningByMediaScanner()

    val fileId = manager.enqueue(request)
    manager.openDownloadedFile(fileId)
  }

  private val manager by lazy {
    requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
  }

  abstract fun download()
}