package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerEducationFragmentBinding
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.EducationType
import com.elka.heofficeclub.other.documents.PostgraduateVocationalEducationType
import com.elka.heofficeclub.other.documents.getEducationSpinnerItems
import com.elka.heofficeclub.other.documents.getPostgraduateEducationSpinnerItems
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseEmployerFragment

class EmployerEducationFragment : BaseEmployerFragment() {
  override val currentScreen: Int = 2
  private lateinit var binding: EmployerEducationFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployerEducationFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerEducationFragment
      viewModel = this@EmployerEducationFragment.viewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val educationsAdapter = SpinnerAdapter(requireContext(), getEducationSpinnerItems())
    binding.educationTypeSpinner.adapter = educationsAdapter

    val postgEducationsAdapter =
      SpinnerAdapter(requireContext(), getPostgraduateEducationSpinnerItems())
    binding.postgEducationTypeSpinner.adapter = postgEducationsAdapter
  }


  override fun onResume() {
    super.onResume()

    binding.educationTypeSpinner.onItemSelectedListener = educationSpinnerListener
    binding.postgEducationTypeSpinner.onItemSelectedListener = postgEducationSpinnerListener
  }

  override fun onStop() {
    super.onStop()

    binding.educationTypeSpinner.onItemSelectedListener = null
    binding.postgEducationTypeSpinner.onItemSelectedListener = null
  }

  private val postgEducationSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val education = spinner.value as PostgraduateVocationalEducationType
      viewModel.setPostgEducationType(education)
    }
  }

  private val educationSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val education = spinner.value as EducationType
      viewModel.setEducationType(education)
    }
  }

  override fun goNext() {
    val action = R.id.action_employerEducationFragment_to_employerFamilyFragment
    navigate(action)
  }

  override fun goBack() {
    val action = R.id.action_employerEducationFragment_to_employerGeneralInfoFragment
    navigate(action)
  }
}