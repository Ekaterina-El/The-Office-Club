package com.elka.heofficeclub.view.ui.createOrganization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.CreateOrganizationFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment

class CreateOrganizationFragment: BaseFragment() {
    private lateinit var binding: CreateOrganizationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateOrganizationFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            master = this@CreateOrganizationFragment
        }

        return binding.root
    }

    fun goBack() {
        navController.popBackStack()
    }

    fun tryRegistration() {
        Toast.makeText(requireContext(), "Registration", Toast.LENGTH_SHORT).show()
    }
}