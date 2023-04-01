package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.ChangeHeadDialogBinding
import com.elka.heofficeclub.databinding.GetVacationDialogBinding
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.User
import com.elka.heofficeclub.view.list.users.SpinnerUsersAdapter
import com.elka.heofficeclub.view.ui.BaseFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.EmployerViewModel
import com.elka.heofficeclub.viewModel.GetVacationViewModel
import java.util.*

open class GetVacationDialog(context: Context, private val owner: BaseFragmentWithDatePicker) : Dialog(context) {
  private lateinit var binding: GetVacationDialogBinding

  private val viewModel: GetVacationViewModel by lazy { ViewModelProvider(owner)[GetVacationViewModel::class.java] }

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
    setCancelable(true)

  }


  fun open(organization: Organization?, employer: Employer?) {
    viewModel.setOrganization(organization)
    viewModel.setEmployer(employer)
    viewModel.clear()
    show()
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


  private val datePickerListener = object : BaseFragmentWithDatePicker.Companion.DatePickerListener {
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
      fun agree(user: User)
    }
  }
}