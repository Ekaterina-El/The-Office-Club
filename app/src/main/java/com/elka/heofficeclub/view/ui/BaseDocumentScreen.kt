package com.elka.heofficeclub.view.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.viewModel.OrganizationDocsViewModel

abstract class BaseDocumentScreen: BaseFragmentWithOrganization() {
  protected val documentsViewModel by activityViewModels<OrganizationDocsViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        goBack()
      }
    })
  }

  fun goBack() {
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }
}