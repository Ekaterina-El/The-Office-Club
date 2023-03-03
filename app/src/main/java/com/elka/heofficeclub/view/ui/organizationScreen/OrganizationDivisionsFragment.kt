package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.AboutOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.OrganizationDivisionsFragmentBinding
import com.elka.heofficeclub.databinding.OrganizationEmplyeesFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.OrganizationDivisionsViewModel

class OrganizationDivisionsFragment: BaseFragmentWithOrganization() {
    private lateinit var binding: OrganizationDivisionsFragmentBinding
    private lateinit var viewModel: OrganizationDivisionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[OrganizationDivisionsViewModel::class.java]
        binding = OrganizationDivisionsFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            master = this@OrganizationDivisionsFragment
            viewModel = this@OrganizationDivisionsFragment.viewModel
        }

        return binding.root
    }

    fun addDivision() {

    }
}