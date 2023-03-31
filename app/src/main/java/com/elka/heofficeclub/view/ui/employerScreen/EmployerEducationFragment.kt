package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.databinding.EmployerEducationFragmentBinding
import com.elka.heofficeclub.databinding.EmployerGeneralInfoFragmentBinding
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.HashMap

class EmployerEducationFragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: EmployerEducationFragmentBinding
  private val viewModel by activityViewModels<EmployerViewModel>()

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

}