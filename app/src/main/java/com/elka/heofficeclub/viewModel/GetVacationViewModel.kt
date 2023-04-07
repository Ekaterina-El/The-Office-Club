package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.T6
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.OrganizationRepository
import kotlinx.coroutines.launch
import java.util.*

class GetVacationViewModel(application: Application) : BaseViewModelWithFields(application) {
  private val _workIntervalStart = MutableLiveData<Date?>(null)
  val workIntervalStart get() = _workIntervalStart

  private val _workIntervalEnd = MutableLiveData<Date?>(null)
  val workIntervalEnd get() = _workIntervalEnd

  private val _vacationAStart = MutableLiveData<Date?>(null)
  val vacationAStart get() = _vacationAStart

  private val _vacationAEnd = MutableLiveData<Date?>(null)
  val vacationAEnd get() = _vacationAEnd

  val vacationBDescription = MutableLiveData("")

  private val _vacationBStart = MutableLiveData<Date?>(null)
  val vacationBStart get() = _vacationBStart

  private val _vacationBEnd = MutableLiveData<Date?>(null)
  val vacationBEnd get() = _vacationBEnd

  private val _vacation = MutableLiveData<T6?>(null)
  val vacation get() = _vacation
  var vacations = listOf<Vacation>()

  private val getVacationWork = Work.GET_VACATION

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
    _vacation.value = null
    vacations = listOf()
    _externalAction.value = null
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

    _vacation.value = getT6(0)

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

    addWork(getVacationWork)

    viewModelScope.launch {
      _error.value = OrganizationRepository.regNextOrderNumber(organization!!.id) { number ->
        _vacation.value = getT6(number)

        // generate PDF file
        _externalAction.value = Action.GENERATE_T6
      }
    }
  }

  override fun checkFields(): Boolean {
    val res = super.checkFields()
    if (!res) return res

    val errors = _fieldErrors.value!!.toMutableList()

    if (_workIntervalEnd.value == null || _workIntervalStart.value == null) {
      errors.add(FieldError(Field.WORK_INTERVAL, FieldErrorType.IS_REQUIRE))
    }

    val vacationAIsFillOut = (_vacationAStart.value != null && _vacationAEnd.value != null)
    val vacationBIsFillOut = (_vacationBStart.value != null && _vacationBEnd.value != null)
    if (!vacationAIsFillOut && !vacationBIsFillOut) {
      errors.add(FieldError(Field.VACATION_INTERVAL, FieldErrorType.EMPTY_ANY_VACATION))
    }

    if (vacationBIsFillOut && vacationBDescription.value!! == "") {
      errors.add(FieldError(Field.VACATION_B_DESCRIPTION, FieldErrorType.IS_REQUIRE))
    }

    _fieldErrors.value = errors
    return errors.isEmpty()
  }


  override val fields = hashMapOf(
    Pair(Field.WORK_INTERVAL, _workIntervalStart as MutableLiveData<Any?>),
    Pair(Field.WORK_INTERVAL, _workIntervalEnd as MutableLiveData<Any?>),
    Pair(Field.VACATION_B_DESCRIPTION, vacationBDescription as MutableLiveData<Any?>),
  )

  fun getT6(number: Int) = T6(
    number = number,
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

  fun saveT6(t6: T6, uri: Uri) {
    viewModelScope.launch {
      // load file to server
      _error.value = DocumentsRepository.setFile(organization!!.id, uri) { fileUri ->

        // set file uri to t6
        t6.fileUrl = fileUri.toString()

        // save t6 to server
        _error.value = DocumentsRepository.setT6(t6) { newT6, vacations ->
          _vacation.value = newT6
          this@GetVacationViewModel.vacations = vacations
          removeWork(getVacationWork)
          _externalAction.value = Action.AFTER_GET_VACATION
        }
      }
    }
  }
}