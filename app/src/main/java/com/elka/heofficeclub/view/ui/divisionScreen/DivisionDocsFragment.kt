package com.elka.heofficeclub.view.ui.divisionScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionDocsFragmentBinding
import com.elka.heofficeclub.databinding.DivisionEmployeesFragmentBinding
import com.elka.heofficeclub.databinding.WelcomeFragmentBinding
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.DivisionViewModel

class DivisionDocsFragment: BaseFragmentWithOrganization() {
    private lateinit var binding: DivisionDocsFragmentBinding
    private val divisionViewModel by activityViewModels<DivisionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DivisionDocsFragmentBinding.inflate(layoutInflater, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@DivisionDocsFragment.divisionViewModel
        }

        return binding.root
    }
}