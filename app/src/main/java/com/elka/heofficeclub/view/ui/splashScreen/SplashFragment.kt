package com.elka.heofficeclub.view.ui.splashScreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.SplashFragmentBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.other.UserStatus
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.service.repository.UsersRepository
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.SplashViewModel

class SplashFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: SplashFragmentBinding
  private lateinit var viewModel: SplashViewModel

  private val handler by lazy { Handler(Looper.getMainLooper()) }

  override val externalActionObserver = Observer<Action?> { action ->
    if (action == null) return@Observer
    when (action) {
      Action.GO_LOGIN -> goLogin()
      Action.GO_ORGANIZATION -> organizationViewModel.loadProfile()
      Action.RESTART -> restartApp()
      else -> Unit
    }
  }

  private val profileObserver = Observer<User?> { user ->
    if (user == null) return@Observer

    when (user.status) {
      UserStatus.UNBLOCKED -> goOrganization()
      UserStatus.BLOCKED -> showDialogAboutBlocked()
    }
  }

  private fun showDialogAboutBlocked() {
    val title = getString(R.string.account_have_been_blocked)
    val message = getString(R.string.account_have_been_blocked_message)
    activity.informDialog.open(title, message, onButtonListener = { logoutWithoutConfirm() })
  }

  private fun goOrganization() {
    val direction = SplashFragmentDirections.actionSplashFragmentToOrganizationFragment2()
    findNavController().navigate(direction)
  }

  private fun goLogin() {
    handler.postDelayed({
      val direction = SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
      findNavController().navigate(direction)
    }, Constants.LOAD_DELAY)

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
    requireActivity().window.statusBarColor = requireContext().getColor(R.color.white)
    binding = SplashFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
    }

    return binding.root
  }

  override fun onStart() {
    super.onStart()
    viewModel.checkLoginStatus()
  }

  override fun onResume() {
    super.onResume()
    viewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
    organizationViewModel.profile.observe(viewLifecycleOwner, profileObserver)
    organizationViewModel.externalAction.observe(viewLifecycleOwner, externalActionObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.externalAction.removeObserver(externalActionObserver)
    organizationViewModel.profile.removeObserver(profileObserver)
    organizationViewModel.externalAction.removeObserver(externalActionObserver)
  }
}