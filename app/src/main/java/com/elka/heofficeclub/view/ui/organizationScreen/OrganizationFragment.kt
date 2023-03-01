package com.elka.heofficeclub.view.ui.organizationScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.CreateOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.OrganizationScreenBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment

class OrganizationFragment: BaseFragment() {
    private lateinit var binding: OrganizationScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OrganizationScreenBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }
}