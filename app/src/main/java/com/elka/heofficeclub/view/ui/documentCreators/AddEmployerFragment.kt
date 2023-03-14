package com.elka.heofficeclub.view.ui.documentCreators

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.elka.heofficeclub.databinding.AddEmployerBinding
import com.elka.heofficeclub.other.DateType
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithOrganization
import com.elka.heofficeclub.viewModel.AddEmployerViewModel
import java.util.*

class AddEmployerFragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: AddEmployerBinding
  private val viewModel by activityViewModels<AddEmployerViewModel>()

  private val positionsObserver = Observer<List<OrganizationPosition>> {
    val spinnerAdapter = OrgPositionsSpinnerAdapter(requireContext(), it)
    binding.positionSpinner.adapter = spinnerAdapter
  }

  private val divisionsObserver = Observer<List<Division>> {
    val spinnerAdapter = DivisionsSpinnerAdapter(requireContext(), it)
    binding.divisionsSpinner.adapter = spinnerAdapter
  }

  private val fieldErrorsObserver = Observer<List<FieldError>> {
    showErrors(it)
  }

  private fun showErrors(errors: List<FieldError>?) {
    binding.layoutFullName.error = ""
    binding.layoutNumber.error = ""
    binding.contractDateError.text = ""
    binding.workPeriodError.text = ""

    if (errors == null) return

    for (error in errors) {
      val errorStr = getString(error.errorType!!.messageRes)
      when (error.field) {
        Field.NAME -> binding.layoutFullName.error = errorStr
        Field.CONTRACT_NUMBER -> binding.layoutNumber.error = errorStr
        Field.CONTRACT_DATE -> binding.contractDateError.text = errorStr
        Field.START_WORK_DATE -> binding.workPeriodError.text = errorStr
        else -> Unit
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = AddEmployerBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@AddEmployerFragment
      viewModel = this@AddEmployerFragment.viewModel
    }

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    organizationViewModel.setBottomMenuStatus(false)
    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        goBack()
      }
    })
  }

  fun goBack() {
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
    viewModel.clear()
  }

  private val divisionSpinnerListener by lazy {
    Selector {
      viewModel.selectDivision(it as Division)
    }
  }

  private val positionSpinnerListener by lazy {
    Selector {
      viewModel.selectPosition(it as OrganizationPosition)
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.divisions.observe(this, divisionsObserver)
    viewModel.positions.observe(this, positionsObserver)
    viewModel.error.observe(this, errorObserver)
    viewModel.fieldErrors.observe(this, fieldErrorsObserver)

    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener

  }

  override fun onStop() {
    super.onStop()
    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)
    viewModel.error.removeObserver(errorObserver)
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)

    binding.divisionsSpinner.onItemSelectedListener = null
    binding.positionSpinner.onItemSelectedListener = null
  }

  fun save() {
    viewModel.trySave()
  }

  private val datePickerListener = object : Companion.DatePickerListener {
    override fun onPick(date: Date) {
      viewModel.saveDate(date)
    }
  }

  fun showContractDatePicker() = showDatePicker(DateType.CONTRACT)
  fun showStartWorkDatePicker() = showDatePicker(DateType.START_WORK)
  fun showEndWorkDatePicker() = showDatePicker(DateType.END_WORK)

  private fun showDatePicker(type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.CONTRACT -> viewModel.contractDate.value
      DateType.START_WORK -> viewModel.startWorkDate.value
      DateType.END_WORK -> viewModel.endWorkDate.value
      else -> null
    }
    showDatePickerDialog(date, datePickerListener)
  }
}

