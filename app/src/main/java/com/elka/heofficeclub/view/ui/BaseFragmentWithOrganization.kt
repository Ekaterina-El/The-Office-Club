package com.elka.heofficeclub.view.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.viewModel.OrganizationViewModel

abstract class BaseFragmentWithOrganization: BaseFragment() {
  protected val organizationViewModel by activityViewModels<OrganizationViewModel>()

  protected open val externalActionObserver = Observer<Action?> { action ->
    if (action == Action.RESTART) restartApp()
  }

  fun logout() {
    // show dialog
    organizationViewModel.logout()
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    organizationViewModel.externalAction.removeObserver(externalActionObserver)
  }
}