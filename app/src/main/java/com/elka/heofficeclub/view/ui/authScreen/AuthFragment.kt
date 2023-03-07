package com.elka.heofficeclub.view.ui.authScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.AuthorizationFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.AuthViewModel

class AuthFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: AuthorizationFragmentBinding
  private lateinit var authViewModel: AuthViewModel

  override val externalActionObserver = Observer<Action?> { action ->
    when (action) {
      Action.GO_NEXT -> navigateToOrganization()
      Action.LOAD_PROFILE -> organizationViewModel.loadProfile()
      else -> Unit
    }
  }

  private val profileObserver = Observer<User?> { user ->
    if (user == null) return@Observer

    when (user.status) {
      UserStatus.UNBLOCKED -> navigateToOrganization()
      UserStatus.BLOCKED -> showDialogAboutBlocked()
    }
  }

  private fun showDialogAboutBlocked() {
    val title = getString(R.string.account_have_been_blocked)
    val message = getString(R.string.account_have_been_blocked_message)
    activity.informDialog.open(title, message, cancelable = false)
    logoutWithoutConfirm()
    authViewModel.clearFields()
  }

  private fun navigateToOrganization() {
    setCredentials(
      Credentials(
        authViewModel.email.value!!,
        authViewModel.password.value!!
      )
    )
    authViewModel.clearFields()
    navController.navigate(AuthFragmentDirections.actionAuthFragmentToOrganizationFragment2())
  }

  private val fieldErrorsObserver = Observer<List<FieldError>> {
    showErrors(it)
  }

  private fun showErrors(errors: List<FieldError>?) {
    binding.layoutEmail.error = ""
    binding.layoutPassword.error = ""

    if (errors == null) return

    for (error in errors) {
      val field = when (error.field) {
        Field.PASSWORD -> binding.layoutPassword
        Field.EMAIL -> binding.layoutEmail
        else -> continue
      }

      field.error = getString(error.errorType!!.messageRes)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
    binding = AuthorizationFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@AuthFragment.authViewModel
      master = this@AuthFragment
    }

    return binding.root
  }

  override fun onResume() {
    super.onResume()
    authViewModel.error.observe(viewLifecycleOwner, errorObserver)
    authViewModel.fieldErrors.observe(viewLifecycleOwner, fieldErrorsObserver)
    authViewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
    authViewModel.work.observe(viewLifecycleOwner, workObserver)
    organizationViewModel.profile.observe(viewLifecycleOwner, profileObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    authViewModel.error.removeObserver(errorObserver)
    authViewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    authViewModel.externalAction.removeObserver(externalActionObserver)
    authViewModel.work.removeObserver(workObserver)
    organizationViewModel.profile.removeObserver(profileObserver)
  }

  fun goBack() {
    navController.popBackStack()
  }

  fun tryLogin() {
    authViewModel.tryLogin()
  }
}