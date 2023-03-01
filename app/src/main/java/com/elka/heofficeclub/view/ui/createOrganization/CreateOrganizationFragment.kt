package com.elka.heofficeclub.view.ui.createOrganization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.CreateOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.viewModel.CreateOrganizationViewModel

class CreateOrganizationFragment: BaseFragment() {
    private lateinit var binding: CreateOrganizationFragmentBinding
    private lateinit var viewModel: CreateOrganizationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[CreateOrganizationViewModel::class.java]

        binding = CreateOrganizationFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            master = this@CreateOrganizationFragment
            viewModel = this@CreateOrganizationFragment.viewModel
        }

        return binding.root
    }

    fun goBack() {
        navController.popBackStack()
        viewModel.clearFields()
    }

    fun tryRegistration() {
        viewModel.tryCreateOrganization()
    }
}