package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerT1FragmentBinding
import com.elka.heofficeclub.other.Constants
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.view.dialog.ChangeWorkPlaceDialog
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseEmployerFragment
import com.elka.heofficeclub.viewModel.DivisionsViewModel
import java.util.HashMap

class EmployerT1Fragment : BaseEmployerFragment() {
  override val currentScreen: Int = 6
  private lateinit var binding: EmployerT1FragmentBinding

  private val divisionsViewModel by activityViewModels<DivisionsViewModel>()

  private val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> {
    showErrors(it, fields)
  }

  private val fields: HashMap<Field, Any> by lazy {
    hashMapOf(
      Pair(Field.CONTRACT_DATE, binding.contractDateError),
      Pair(Field.CONTRACT_NUMBER, binding.layoutContractNumber),
    )
  }

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
    viewModel.fieldErrors.observe(this, fieldErrorsObserver)

    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener
  }

  override fun onStop() {
    super.onStop()

    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)

    binding.divisionsSpinner.onItemSelectedListener = null
    binding.positionSpinner.onItemSelectedListener = null
  }

  val changeWorkPlaceListener by lazy {
    object: ChangeWorkPlaceDialog.Companion.Listener {
      override fun onChangeWork(t5: T5) {

      }
    }
  }

  private val changeWorkPlaceDialog by lazy { ChangeWorkPlaceDialog(requireContext(), this, changeWorkPlaceListener) }
  fun changeWork() {
    val positions = viewModel.positions.value

    changeWorkPlaceDialog.open(
      organizationViewModel.organization.value!!,
      viewModel.employer.value!!,
      divisionsViewModel.divisions.value!!,
      viewModel.positions.value!!
    )
  }

  fun showContractDatePicker() =
    if (viewModel.isCreation.value!!) showDatePicker(viewModel, DateType.CONTRACT) else Unit

  fun showStartWorkDatePicker() =
    if (viewModel.isCreation.value!!) showDatePicker(viewModel, DateType.START_WORK) else Unit

  fun showEndWorkDatePicker() =
    if (viewModel.isCreation.value!!) showDatePicker(viewModel, DateType.END_WORK) else Unit
}