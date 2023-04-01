package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.T6
import java.util.*

class GetVacationViewModel(application: Application) : BaseViewModel(application) {
  private val _workIntervalStart = MutableLiveData<Date?>(null)
  val workIntervalStart get() = _workIntervalStart
  fun setWorkIntervalStart(date: Date) {
    _workIntervalStart.value = date
  }

  private val _workIntervalEnd = MutableLiveData<Date?>(null)
  val workIntervalEnd get() = _workIntervalEnd
  fun setWorkIntervalEnd(date: Date) {
    _workIntervalEnd.value = date
  }

  private val _vacationAStart = MutableLiveData<Date?>(null)
  val vacationAStart get() = _vacationAStart
  fun setVacationAStart(date: Date) {
    _vacationAStart.value = date
  }

  private val _vacationAEnd = MutableLiveData<Date?>(null)
  val vacationAEnd get() = _vacationAEnd
  fun setVacationAEnd(date: Date) {
    _vacationAEnd.value = date
  }

  val vacationBDescription = MutableLiveData("")

  private val _vacationBStart = MutableLiveData<Date?>(null)
  val vacationBStart get() = _vacationBStart
  fun setVacationBStart(date: Date) {
    _vacationBStart.value = date
  }

  private val _vacationBEnd = MutableLiveData<Date?>(null)
  val vacationBEnd get() = _vacationBEnd
  fun setVacationBEnd(date: Date) {
    _vacationBEnd.value = date
  }

  fun clear() {
    _workIntervalStart.value = null
    _workIntervalEnd.value = null
    _vacationAStart.value = null
    _vacationAEnd.value = null
    _vacationBStart.value = null
    _vacationBEnd.value = null
    vacationBDescription.value = ""
    employer = null
    organization = null
  }

  private val _editDate = MutableLiveData<DateType?>(null)
  fun setEditTime(type: DateType) {
    _editDate.value = type
  }

  fun saveDate(date: Date) {
    when (_editDate.value) {
      DateType.WORK_INTERVAL_START -> _workIntervalStart
      DateType.WORK_INTERVAL_END -> _workIntervalEnd
      DateType.VACATION_A_START -> _vacationAStart
      DateType.VACATION_A_END -> _vacationAEnd
      DateType.VACATION_B_START -> _vacationBStart
      DateType.VACATION_B_END -> _vacationBEnd
      else -> return
    }.value = date
  }

  private var organization: Organization? = null
  fun setOrganization(organization: Organization?) {
    this.organization = organization
  }

  private var employer: Employer? = null
  fun setEmployer(emp: Employer?) {
    employer = emp
  }

  fun trySave() {
    if (!checkFields()) return
    val vacation = vacation
    Log.d("trySave", "SAVE $vacation")
  }

  private fun checkFields(): Boolean {
    return true
  }

  val vacation get() = T6(
    dataCreated = Calendar.getInstance().time,
    startWork = workIntervalStart.value,
    endWork = workIntervalEnd.value,
    startVacationA = vacationAStart.value,
    endVacationA = vacationAEnd.value,
    startVacationB = vacationBStart.value,
    endVacationB = vacationBEnd.value,
    vacationBDescription = vacationBDescription.value!!,
    employer = employer,
    division = employer?.divisionLocal,
    position = employer?.positionLocal,
    organization = organization
  )
}