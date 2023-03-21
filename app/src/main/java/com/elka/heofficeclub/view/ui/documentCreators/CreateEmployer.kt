package com.elka.heofficeclub.view.ui.documentCreators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.elka.heofficeclub.databinding.CreateEmployerBinding
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.view.list.users.SpinnerAdapter

class CreateEmployerFragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: CreateEmployerBinding
  private val viewModel by activityViewModels<CreateEmployerViewModel>()


  override val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.CREATE_FILE ->
        Toast.makeText(requireContext(), "Create file!", Toast.LENGTH_SHORT).show()

      Action.GO_BACK -> goBack()

      else -> Unit
    }
  }

  private val positionsObserver = Observer<List<OrganizationPosition>> {
    val spinnerAdapter = OrgPositionsSpinnerAdapter(requireContext(), it)
//    binding.positionSpinner.adapter = spinnerAdapter
  }

  private val divisionsObserver = Observer<List<Division>> {
    val spinnerAdapter = DivisionsSpinnerAdapter(requireContext(), it)
//    binding.divisionsSpinner.adapter = spinnerAdapter
  }

  private val fieldErrorsObserver = Observer<List<FieldError>> {
    showErrors(it)
  }

  private fun showErrors(errors: List<FieldError>?) {

  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = CreateEmployerBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@CreateEmployerFragment
      viewModel = this@CreateEmployerFragment.viewModel
    }

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    organizationViewModel.setBottomMenuStatus(false)
    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        viewModel.goPrevScreen()
      }
    })
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initSpinners()
  }

  private fun initSpinners() {
    // Genders spinner
    val spinnerAdapter = SpinnerAdapter(requireContext(), getGenderSpinnerItems())
    binding.genderSpinner.adapter = spinnerAdapter

    // Education spinner
    val educationsAdapter = SpinnerAdapter(requireContext(), getEducationSpinnerItems())
    binding.educationTypeSpinner.adapter = educationsAdapter

    // Postg education spinner
    val postgEducationsAdapter = SpinnerAdapter(requireContext(), getPostgraduateEducationSpinnerItems())
    binding.postgEducationTypeSpinner.adapter = postgEducationsAdapter
  }

  private fun goBack() {
    viewModel.clear()
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }

  override fun onResume() {
    super.onResume()
    viewModel.externalAction.observe(this, externalActionObserver)
    binding.genderSpinner.onItemSelectedListener = genderSpinnerListener
    binding.educationTypeSpinner.onItemSelectedListener = educationSpinnerListener
    binding.postgEducationTypeSpinner.onItemSelectedListener = postgEducationSpinnerListener


//    viewModel.divisions.observe(this, divisionsObserver)
//    viewModel.positions.observe(this, positionsObserver)
//    viewModel.error.observe(this, errorObserver)
//    viewModel.fieldErrors.observe(this, fieldErrorsObserver)
//
//    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
//    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener

  }

  override fun onStop() {
    super.onStop()
    viewModel.externalAction.removeObserver(externalActionObserver)
    binding.genderSpinner.onItemSelectedListener = null
    binding.educationTypeSpinner.onItemSelectedListener = null
    binding.postgEducationTypeSpinner.onItemSelectedListener = null


//    viewModel.divisions.removeObserver(divisionsObserver)
//    viewModel.positions.removeObserver(positionsObserver)
//    viewModel.error.removeObserver(errorObserver)
//    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
//
//    binding.divisionsSpinner.onItemSelectedListener = null
  }

  private val genderSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val gender = spinner.value as Gender
      viewModel.setGender(gender)
    }
  }

  private val educationSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val education = spinner.value as EducationType
      viewModel.setEducationType(education)
    }
  }

  private val postgEducationSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val education = spinner.value as PostgraduateVocationalEducationType
      viewModel.setPostgEducationType(education)
    }
  }

  private val datePickerListener = object : Companion.DatePickerListener {
    override fun onPick(date: Date) {
      viewModel.saveDate(date)
    }
  }

  fun showRegAccorinigAddressPicker() = showDatePicker(DateType.REG_ACCORINING_ADDRESS)
  fun showBirthdatePicker() = showDatePicker(DateType.BIRTDAY)
  fun showPassportDatePicker() = showDatePicker(DateType.PASSPORT_DATE)

  private fun showDatePicker(type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.BIRTDAY -> viewModel.birthdate.value
      DateType.PASSPORT_DATE -> viewModel.passportDate.value
      DateType.REG_ACCORINING_ADDRESS -> viewModel.dateOfRegAccorinigAddress.value
      else -> null
    }
    showDatePickerDialog(date, datePickerListener)
  }
}