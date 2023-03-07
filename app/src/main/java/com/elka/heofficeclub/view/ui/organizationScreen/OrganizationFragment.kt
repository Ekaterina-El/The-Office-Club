package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.CreateOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.OrganizationScreenBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationViewModel

class OrganizationFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: OrganizationScreenBinding

  private val profileObserver = Observer<User?> { profile ->
    if (profile == null) return@Observer
    youAuthedAs(profile.role)
    organizationViewModel.loadOrganization(profile.organizationId)
  }

  private fun youAuthedAs(role: Role) {
    val s = getString(R.string.youAuthedAs, getString(role.resId))
    Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    requireActivity().window.statusBarColor = requireContext().getColor(R.color.primary_dark)

    binding = OrganizationScreenBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@OrganizationFragment
      viewModel = organizationViewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val navController =
      (childFragmentManager.findFragmentById(R.id.organizationContainer) as NavHostFragment)
        .navController
    binding.bottomMenu.setupWithNavController(navController)
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.profile.observe(viewLifecycleOwner, profileObserver)
    organizationViewModel.error.observe(viewLifecycleOwner, errorObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    organizationViewModel.profile.removeObserver(profileObserver)
    organizationViewModel.error.removeObserver(errorObserver)
  }


}