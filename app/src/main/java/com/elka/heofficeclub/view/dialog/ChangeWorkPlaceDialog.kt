package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.ChangeWorkPlaceDialogBinding
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.ChangeWorkPlaceViewModel
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.*

open class ChangeWorkPlaceDialog(
  context: Context, private val owner: BaseFragmentWithDatePicker, val listener: Listener
) : Dialog(context) {
  private lateinit var binding: ChangeWorkPlaceDialogBinding
  private val viewModel: ChangeWorkPlaceViewModel by lazy { ViewModelProvider(owner)[ChangeWorkPlaceViewModel::class.java] }


  private val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> { errors ->
    owner.showErrors(errors, fields)
  }

  private val workObserver = androidx.lifecycle.Observer<List<Work>> {
    setCancelable(it.isEmpty())
  }

  private val actionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.GENERATE_T5 -> generateT5()
      Action.AFTER_CHANGE_WORK -> changeWorkAndBack()
      else -> Unit
    }
  }

  private fun changeWorkAndBack() {
    listener.onChangeWork(viewModel.getT5())
    viewModel.clear()
    disagree()
  }

  private fun generateT5() {
//    val t5 = viewModel.getT5()
//    val uri = DocumentCreator(context).createFormT5(t5)
//    viewModel.saveT5(t5, uri)
  }

  private val fields: HashMap<Field, Any> by lazy {
    hashMapOf()
  }

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = ChangeWorkPlaceDialogBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@ChangeWorkPlaceDialog
      viewModel = this@ChangeWorkPlaceDialog.viewModel
      lifecycleOwner = owner
    }
    setContentView(binding.root)
    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    initSpinners()
  }

  private fun initSpinners() {
    val typeOfChangeWorkAdapter = SpinnerAdapter(context, getTypeOfChangeWorkItems())
    binding.typesOfChangeWorkSpinner.adapter = typeOfChangeWorkAdapter

    val foundationTypeAdapter = SpinnerAdapter(context, getFoundationTypeItems())
    binding.foundationTypeSpinner.adapter = foundationTypeAdapter
  }

  fun open(organization: Organization, employer: Employer, divisions: List<Division>, positions: List<OrganizationPosition>) {
    viewModel.clear()
    viewModel.initData(organization, employer, divisions, positions)
    show()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()

    viewModel.work.observe(owner, workObserver)
    viewModel.externalAction.observe(owner, actionObserver)
    viewModel.divisions.observe(owner, divisionsObserver)
    viewModel.positions.observe(owner, positionsObserver)

    binding.typesOfChangeWorkSpinner.onItemSelectedListener = typesOfChangeWorkListener
    binding.foundationTypeSpinner.onItemSelectedListener = foundationTypeListener
    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener

  }

  private val typesOfChangeWorkListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val typeOfChangeWork = spinner.value as TypeOfChangeWork
      viewModel.selectTypeOfChangeWork(typeOfChangeWork)
    }
  }

  private val foundationTypeListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val foundationType = spinner.value as FoundationType
      viewModel.selectFoundationType(foundationType)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
//    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.externalAction.removeObserver(actionObserver)
    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)

    binding.typesOfChangeWorkSpinner.onItemSelectedListener = null
    binding.foundationTypeSpinner.onItemSelectedListener = null
    binding.divisionsSpinner.onItemSelectedListener = null
    binding.positionSpinner.onItemSelectedListener = null
  }

  fun disagree() {
    dismiss()
  }

  fun agree() {
    viewModel.trySave()
  }

  fun showTransferStartDatePicker() = showDatePicker(DateType.START_TRANSFER)
  fun showTransferEndDatePicker() = showDatePicker(DateType.END_TRANSFER)

  private val datePickerListener =
    object : BaseFragmentWithDatePicker.Companion.DatePickerListener {
      override fun onPick(date: Date) {
        viewModel.saveDate(date)
      }
    }

  private fun showDatePicker(type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.START_TRANSFER -> viewModel.transferStart.value
      DateType.END_TRANSFER -> viewModel.transferEnd.value
      else -> null
    }
    owner.showDatePickerDialog(date, datePickerListener)
  }

  private val divisionsObserver = androidx.lifecycle.Observer<List<Division>> {
    val spinnerAdapter = DivisionsSpinnerAdapter(context, it)
    binding.divisionsSpinner.adapter = spinnerAdapter
  }

  private val positionsObserver = androidx.lifecycle.Observer<List<OrganizationPosition>> {
    val spinnerAdapter = OrgPositionsSpinnerAdapter(context, it)
    binding.positionSpinner.adapter = spinnerAdapter
  }

  private val divisionSpinnerListener by lazy {
    Selector { viewModel.selectDivision(it as Division) }
  }

  private val positionSpinnerListener by lazy {
    Selector {
      val position = it as OrganizationPosition
      viewModel.selectPosition(position)
    }
  }

  companion object {
    interface Listener {
      fun onChangeWork(t5: T5)
    }
  }
}