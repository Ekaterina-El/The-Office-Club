package com.elka.heofficeclub.view.ui

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.view.dialog.ConfirmDialog
import com.elka.heofficeclub.viewModel.OrganizationViewModel

abstract class BaseFragmentWithOrganization: BaseFragment() {
  val organizationViewModel by activityViewModels<OrganizationViewModel>()

  protected open val externalActionObserver = Observer<Action?> { action ->
    if (action == Action.RESTART) restartApp()
  }

  private val exitListener by lazy {
    object: ConfirmDialog.Companion.Listener {
      override fun agree() {
        confirmDialog.close()
        setCredentials(null)
        activity.appDatabase.clear()
        organizationViewModel.logout()
      }

      override fun disagree() {
        confirmDialog.close()
      }

    }
  }
  fun logoutWithoutConfirm() {
    setCredentials(null)
    organizationViewModel.logout()
  }

  fun logout() {
    val title = getString(R.string.exit_title)
    val message = getString(R.string.exit_message)
    confirmDialog.open(title, message, exitListener)
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