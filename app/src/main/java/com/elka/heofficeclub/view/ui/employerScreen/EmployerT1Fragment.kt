package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerT1FragmentBinding
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.BaseViewModelEmployer
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.*

class EmployerT1Fragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: EmployerT1FragmentBinding
  private val viewModel by activityViewModels<EmployerViewModel>()

  private val divisionsObserver = androidx.lifecycle.Observer<List<Division>> {
    val spinnerAdapter = DivisionsSpinnerAdapter(requireContext(), it)
    binding.divisionsSpinner.adapter = spinnerAdapter
  }

  private val positionsObserver = androidx.lifecycle.Observer<List<OrganizationPosition>> {
    val items = it.toMutableList()
    items.add(
      OrganizationPosition(
        id = Constants.RESERVED_TO_ADD,
        name = getString(R.string.add_position)
      )
    )
    val spinnerAdapter = OrgPositionsSpinnerAdapter(requireContext(), items)
    binding.positionSpinner.adapter = spinnerAdapter
  }

  private val divisionSpinnerListener by lazy {
    Selector { viewModel.selectDivision(it as Division) }
  }

  private val positionSpinnerListener by lazy {
    Selector {
      val position = it as OrganizationPosition
      if (position.id == Constants.RESERVED_TO_ADD) {
        addOrganizationPosition()
      } else viewModel.selectPosition(position)
    }
  }

  private val organizationPositionDialogListener by lazy {
    object : OrganizationPositionDialog.Companion.Listener {
      override fun agree(organizationPosition: OrganizationPosition) {
        organizationViewModel.addPosition(organizationPosition)
        viewModel.addPosition(organizationPosition)
      }
    }
  }

  private val organizationPositionDialog by lazy {
    OrganizationPositionDialog(
      requireContext(),
      viewModelOwner = this,
      viewLifecycleOwner,
      organizationViewModel.organization.value!!.id,
      organizationPositionDialogListener
    )
  }

  private fun addOrganizationPosition() {
    organizationPositionDialog.open()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployerT1FragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerT1Fragment
      viewModel = this@EmployerT1Fragment.viewModel
    }

    return binding.root
  }

  override fun onResume() {
    super.onResume()

    viewModel.divisions.observe(this, divisionsObserver)
    viewModel.positions.observe(this, positionsObserver)

    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener
  }

  override fun onStop() {
    super.onStop()

    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)

    binding.divisionsSpinner.onItemSelectedListener = null
    binding.positionSpinner.onItemSelectedListener = null
  }

  fun showContractDatePicker() =
    if (viewModel.isCreation.value!!) showDatePicker(viewModel, DateType.CONTRACT) else Unit

  fun showStartWorkDatePicker() =
    if (viewModel.isCreation.value!!) showDatePicker(viewModel, DateType.START_WORK) else Unit

  fun showEndWorkDatePicker() =
    if (viewModel.isCreation.value!!) showDatePicker(viewModel, DateType.END_WORK) else Unit

  private val datePickerListener = object : Companion.DatePickerListener {
    override fun onPick(date: Date) {
      viewModel.saveDate(date)
    }
  }


  private fun showDatePicker(viewModel: BaseViewModelEmployer, type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.CONTRACT -> viewModel.contractDate.value
      DateType.START_WORK -> viewModel.hiredFrom.value
      DateType.END_WORK -> viewModel.hiredBy.value
      else -> null
    }
    showDatePickerDialog(date, datePickerListener)
  }
}