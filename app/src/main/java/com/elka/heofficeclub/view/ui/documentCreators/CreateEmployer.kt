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
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.elka.heofficeclub.databinding.CreateEmployerBinding
import com.elka.heofficeclub.databinding.SpinnerOneLineBinding
import com.elka.heofficeclub.other.documents.Gender
import com.elka.heofficeclub.view.list.users.GendersAdapter

class CreateEmployerFragment: BaseFragmentWithDatePicker() {
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
    initGenderSpinner()
  }

  private fun initGenderSpinner() {
    val spinnerAdapter = GendersAdapter(requireContext(), Gender.values())
    binding.genderSpinner.adapter = spinnerAdapter
  }

  private fun goBack() {
    viewModel.clear()
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }


  private val divisionSpinnerListener by lazy {
    Selector {
//      viewModel.selectDivision(it as Division)
    }
  }

  private val positionSpinnerListener by lazy {
    Selector {
//      viewModel.selectPosition(it as OrganizationPosition)
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.externalAction.observe(this, externalActionObserver)
    binding.genderSpinner.onItemSelectedListener = genderSpinnerListener


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


//    viewModel.divisions.removeObserver(divisionsObserver)
//    viewModel.positions.removeObserver(positionsObserver)
//    viewModel.error.removeObserver(errorObserver)
//    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
//
//    binding.divisionsSpinner.onItemSelectedListener = null
  }

  private val genderSpinnerListener by lazy {
    Selector {
      val gender = it as Gender
      viewModel.setGender(gender)
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