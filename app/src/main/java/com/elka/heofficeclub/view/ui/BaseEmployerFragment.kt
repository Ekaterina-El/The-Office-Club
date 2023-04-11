package com.elka.heofficeclub.view.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.*

abstract class BaseEmployerFragment: BaseFragmentWithDatePicker() {
  abstract val currentScreen: Int
  protected val viewModel by activityViewModels<EmployerViewModel>()

  private val datePickerListener = object : Companion.DatePickerListener {
    override fun onPick(date: Date) {
      viewModel.saveDate(date)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        goBack()
      }
    })
  }

  protected fun showDatePicker(viewModel: EmployerViewModel, type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.BIRTDAY -> viewModel.birthdate.value
      DateType.PASSPORT_DATE -> viewModel.passportDate.value
      DateType.REG_ACCORINING_ADDRESS -> viewModel.dateOfRegAccorinigAddress.value
      DateType.CONTRACT -> viewModel.contractDate.value
      DateType.START_WORK -> viewModel.hiredFrom.value
      DateType.END_WORK -> viewModel.hiredBy.value
      else -> null
    }
    showDatePickerDialog(date, datePickerListener)
  }

  protected fun navigate(action: Int) {
    navController.navigate(action)
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onStop() {
    super.onStop()
  }

  abstract fun goNext()
  abstract fun goBack()
}