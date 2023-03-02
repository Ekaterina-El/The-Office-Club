package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.CreateOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.OrganizationScreenBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationViewModel

class OrganizationFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: OrganizationScreenBinding

  private val profileObserver = Observer<User?> { profile ->
    if (profile == null) return@Observer
    organizationViewModel.loadOrganization(profile.organizationId)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = OrganizationScreenBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@OrganizationFragment
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    organizationViewModel.loadProfile()

    val navController =
      (childFragmentManager.findFragmentById(R.id.organizationContainer) as NavHostFragment)
        .navController
    binding.bottomMenu.setupWithNavController(navController)
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.profile.observe(viewLifecycleOwner, profileObserver)
    organizationViewModel.error.observe(viewLifecycleOwner, errorObserver)
    organizationViewModel.work.observe(viewLifecycleOwner, workObserver)
  }

  override fun onDestroy() {
    super.onDestroy()
    organizationViewModel.profile.removeObserver(profileObserver)
    organizationViewModel.error.removeObserver(errorObserver)
    organizationViewModel.work.removeObserver(workObserver)
  }


}