package com.elka.heofficeclub.view.ui.organizationScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.AboutOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationAboutViewModel
import com.elka.heofficeclub.viewModel.OrganizationViewModel

class AboutOrganizationFragment : BaseFragmentWithOrganization() {
  private lateinit var binding: AboutOrganizationFragmentBinding
  private lateinit var viewModel: OrganizationAboutViewModel

  private val organizationObserver = Observer<Organization?> { organization ->
    binding.swipeRefreshLayout.isRefreshing = false

    if (organization == null) return@Observer
    viewModel.setOrganization(organization)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProvider(this)[OrganizationAboutViewModel::class.java]
    binding = AboutOrganizationFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      viewModel = this@AboutOrganizationFragment.viewModel
      master = this@AboutOrganizationFragment
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.swipeRefreshLayout.setColorSchemeColors(
      requireContext().getColor(R.color.accent),
      requireContext().getColor(R.color.primary_dark)
    )


    binding.swipeRefreshLayout.setOnRefreshListener {
      organizationViewModel.reloadCurrentOrganization()
    }
  }

  override fun onResume() {
    super.onResume()
    organizationViewModel.organization.observe(viewLifecycleOwner, organizationObserver)
    viewModel.work.observe(viewLifecycleOwner, workObserver)

  }

  override fun onStop() {
    super.onStop()
    organizationViewModel.organization.removeObserver(organizationObserver)
    viewModel.work.removeObserver(workObserver)
  }
}