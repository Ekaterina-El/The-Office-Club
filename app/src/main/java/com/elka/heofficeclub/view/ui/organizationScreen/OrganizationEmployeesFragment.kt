package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.AboutOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.OrganizationEmplyeesFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization

class OrganizationEmployeesFragment: BaseFragmentWithOrganization() {
    private lateinit var binding: OrganizationEmplyeesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrganizationEmplyeesFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }
}