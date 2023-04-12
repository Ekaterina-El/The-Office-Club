package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerT1FragmentBinding
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.service.model.documents.forms.T8
import com.elka.heofficeclub.view.dialog.ChangeWorkPlaceDialog
import com.elka.heofficeclub.view.dialog.DismissalEmployerDialog
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseEmployerFragment
import com.elka.heofficeclub.viewModel.DivisionsViewModel
import java.util.HashMap

class EmployerT1Fragment : BaseEmployerFragment() {
  override val currentScreen: Int = 6
  private lateinit var binding: EmployerT1FragmentBinding

  override val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    if (it == Action.T2_UPDATED) {
      Toast.makeText(requireContext(), getString(R.string.t2_updated), Toast.LENGTH_SHORT).show()
      viewModel.goBack()
    }
  }

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

    viewModel.externalAction.observe(this, externalActionObserver)
    viewModel.divisions.observe(this, divisionsObserver)
    viewModel.positions.observe(this, positionsObserver)
    viewModel.fieldErrors.observe(this, fieldErrorsObserver)

    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener
  }

  override fun onStop() {
    super.onStop()

    viewModel.externalAction.removeObserver(externalActionObserver)
    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)

    binding.divisionsSpinner.onItemSelectedListener = null
    binding.positionSpinner.onItemSelectedListener = null
  }

  val changeWorkPlaceListener by lazy {
    object: ChangeWorkPlaceDialog.Companion.Listener {
      override fun onChangeWork(t5: T5) {
        viewModel.updateWork(t5)
      }
    }
  }

  private val changeWorkPlaceDialog by lazy { ChangeWorkPlaceDialog(requireContext(), this, changeWorkPlaceListener) }
  fun changeWork() {
    changeWorkPlaceDialog.open(
      organizationViewModel.organization.value!!,
      viewModel.employer.value!!,
      divisionsViewModel.divisions.value!!,
      viewModel.positions.value!!
    )
  }

  private val dismissalEmployerListener by lazy {
    object: DismissalEmployerDialog.Companion.Listener {
      override fun onDismissal(t8: T8) {
        viewModel.onDismissal(t8)
      }
    }
  }
  private val dismissalEmployerDialog by lazy { DismissalEmployerDialog(requireContext(), this, dismissalEmployerListener) }
  fun dismissalEmployer() {
    dismissalEmployerDialog.open(organizationViewModel.organization.value!!, viewModel.employer.value!!)
  }

  fun showContractDatePicker() =
    if (viewModel.isCreation.value!! && viewModel.employer.value!!.T8Local == null) showDatePicker(viewModel, DateType.CONTRACT) else Unit

  fun showStartWorkDatePicker() =
    if (viewModel.isCreation.value!! && viewModel.employer.value!!.T8Local == null) showDatePicker(viewModel, DateType.START_WORK) else Unit

  fun showEndWorkDatePicker() =
    if (viewModel.isCreation.value!! && viewModel.employer.value!!.T8Local == null) showDatePicker(viewModel, DateType.END_WORK) else Unit

  override fun goNext() {
    if (!viewModel.checkFieldsCurrentScreen(currentScreen)) return
    viewModel.onEndScreens()
  }

  override fun goBack() {
    val action = R.id.action_employerT1Fragment_to_employerOtherInformationsFragment
    navigate(action)
  }
}