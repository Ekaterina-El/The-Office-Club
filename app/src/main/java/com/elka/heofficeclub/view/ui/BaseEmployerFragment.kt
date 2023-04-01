package com.elka.heofficeclub.view.ui

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


  private val screenObserver = androidx.lifecycle.Observer<Int> {
    val lastScreen = viewModel.lastScreen
    if (it == currentScreen + 1) navigateNext(lastScreen)
    else if (it == currentScreen - 1) navigateBack(lastScreen)
  }

  private fun navigateBack(last: Int) {
    val action = when (last) {
      2 -> R.id.action_employerEducationFragment_to_employerGeneralInfoFragment
      3 -> R.id.action_employerFamilyFragment_to_employerEducationFragment
      4 -> R.id.action_employerMilitaryRegistrationFragment_to_employerFamilyFragment
      5 -> R.id.action_employerOtherInformationsFragment_to_employerMilitaryRegistrationFragment
      6 -> R.id.action_employerT1Fragment_to_employerOtherInformationsFragment
      else -> return
    }
    navController.navigate(action)
  }

  private fun navigateNext(last: Int) {
    if (last != viewModel.screens) {
      val action = when (last) {
        1 -> R.id.action_employerGeneralInfoFragment_to_employerEducationFragment
        2 -> R.id.action_employerEducationFragment_to_employerFamilyFragment
        3 -> R.id.action_employerFamilyFragment_to_employerMilitaryRegistrationFragment
        4 -> R.id.action_employerMilitaryRegistrationFragment_to_employerOtherInformationsFragment
        5 -> R.id.action_employerOtherInformationsFragment_to_employerT1Fragment
        else -> return
      }
      navController.navigate(action)
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.screen.observe(this, screenObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.screen.removeObserver(screenObserver)
  }
}