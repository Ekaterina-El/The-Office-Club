package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.databinding.GetVacationDialogBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.DocumentCreator
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.other.documents.creators.T6Creator
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.GetVacationViewModel
import java.util.*

open class GetVacationDialog(context: Context, private val owner: BaseFragmentWithDatePicker, val listener: Listener) :
  Dialog(context) {
  private lateinit var binding: GetVacationDialogBinding
  private val viewModel: GetVacationViewModel by lazy { ViewModelProvider(owner)[GetVacationViewModel::class.java] }

  private val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> { errors ->
    owner.showErrors(errors, fields)
  }

  private val workObserver = androidx.lifecycle.Observer<List<Work>> {
    setCancelable(it.isEmpty())
  }

  private val actionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.GENERATE_T6 -> generateT6()
      Action.AFTER_GET_VACATION -> getVacationsAndBack()
      else -> Unit
    }
  }

  private fun getVacationsAndBack() {
    listener.addVacations(viewModel.vacations)
    viewModel.clear()
    disagree()
  }

  private fun generateT6() {
    val t6 = viewModel.vacation.value!!
    val uri = DocumentCreator(context).createFormT6(t6)
    viewModel.saveT6(t6, uri)
  }

  private val fields: HashMap<Field, Any> by lazy {
    hashMapOf(
      Pair(Field.WORK_INTERVAL, binding.workIntervalError),
      Pair(Field.VACATION_INTERVAL, binding.vacationIntervalError),
      Pair(Field.VACATION_B_DESCRIPTION, binding.layoutVacationBDesc),
    )
  }

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = GetVacationDialogBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@GetVacationDialog
      viewModel = this@GetVacationDialog.viewModel
      lifecycleOwner = owner
    }
    setContentView(binding.root)

    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }


  fun open(organization: Organization?, employer: Employer?) {
    viewModel.clear()
    viewModel.setOrganization(organization)
    viewModel.setEmployer(employer)
    show()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    viewModel.fieldErrors.observe(owner, fieldErrorsObserver)
    viewModel.work.observe(owner, workObserver)
    viewModel.externalAction.observe(owner, actionObserver)
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.externalAction.removeObserver(actionObserver)
  }

  fun disagree() {
    dismiss()
  }

  fun showWorkIntervalStartDatePicker() = showDatePicker(DateType.WORK_INTERVAL_START)
  fun showWorkIntervalEndDatePicker() = showDatePicker(DateType.WORK_INTERVAL_END)
  fun showVacationAStartDatePicker() = showDatePicker(DateType.VACATION_A_START)
  fun showVacationAEndDatePicker() = showDatePicker(DateType.VACATION_A_END)
  fun showVacationBStartDatePicker() = showDatePicker(DateType.VACATION_B_START)
  fun showVacationBEndDatePicker() = showDatePicker(DateType.VACATION_B_END)

  fun agree() {
    viewModel.trySave()
  }


  private val datePickerListener =
    object : BaseFragmentWithDatePicker.Companion.DatePickerListener {
      override fun onPick(date: Date) {
        viewModel.saveDate(date)
      }
    }

  private fun showDatePicker(type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.WORK_INTERVAL_START -> viewModel.workIntervalStart.value
      DateType.WORK_INTERVAL_END -> viewModel.workIntervalEnd.value
      DateType.VACATION_A_START -> viewModel.vacationAStart.value
      DateType.VACATION_A_END -> viewModel.vacationAEnd.value
      DateType.VACATION_B_START -> viewModel.vacationBStart.value
      DateType.VACATION_B_END -> viewModel.vacationBEnd.value
      else -> null
    }
    owner.showDatePickerDialog(date, datePickerListener)
  }


  companion object {
    interface Listener {
      fun addVacations(vacations: List<Vacation>)
    }
  }
}