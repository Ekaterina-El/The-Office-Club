package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.FoundationType
import com.elka.heofficeclub.other.documents.TypeOfChangeWork
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.OrganizationRepository
import kotlinx.coroutines.launch
import java.util.*

class ChangeWorkPlaceViewModel(application: Application) : BaseViewModel(application) {
  var oldPosition: OrganizationPosition? = null
  var oldDivision: Division? = null

  val premium = MutableLiveData("")
  val transferReason = MutableLiveData("")
  val foundation = MutableLiveData("")

  private val _transferStart = MutableLiveData<Date?>(null)
  val transferStart get() = _transferStart

  private val _transferEnd = MutableLiveData<Date?>(null)
  val transferEnd get() = _transferEnd

  private val _foundationType = MutableLiveData(FoundationType.CONTRACT)
  val foundationType get() = _foundationType
  fun selectFoundationType(foundationType: FoundationType) {
    _foundationType.value = foundationType
  }

  private var typeOfChangeWork: TypeOfChangeWork = TypeOfChangeWork.PERMANENT
  fun selectTypeOfChangeWork(typeOfChangeWork: TypeOfChangeWork) {
    this.typeOfChangeWork = typeOfChangeWork
  }

  private val _divisions = MutableLiveData<List<Division>>()
  val divisions get() = _divisions

  private var _selectedDivision: Division? = null
  fun selectDivision(division: Division) {
    _selectedDivision = division
  }

  private val _positions = MutableLiveData<List<OrganizationPosition>>()
  val positions get() = _positions

  private var _selectedPosition: OrganizationPosition? = null
  fun selectPosition(organizationPosition: OrganizationPosition) {
    _selectedPosition = organizationPosition
  }

  private var _organization: Organization? = null

  private var _employer = MutableLiveData<Employer?>(null)
  val employer get() = _employer

  fun initData(
    organization: Organization,
    employer: Employer,
    divisions: List<Division>,
    positions: List<OrganizationPosition>
  ) {
    oldDivision = employer.divisionLocal
    oldPosition = employer.positionLocal
    _organization = organization
    _employer.value = employer
    _divisions.value = divisions
    _positions.value = positions
  }

  private val changeWorkWork = Work.CHANGE_WORK

  private var number = 0
  fun trySave() {
    if (!checkFields()) return

    addWork(changeWorkWork)
    viewModelScope.launch {
      OrganizationRepository.regNextOrderNumber(_organization!!.id) { number ->
        this@ChangeWorkPlaceViewModel.number = number

        // generate PDF file
        _externalAction.value = Action.GENERATE_T5
      }
    }
  }

  fun checkFields(): Boolean {
    return true
  }

  fun clear() {
    oldDivision = null
    oldPosition = null
    _selectedDivision = null
    _selectedPosition = null
    typeOfChangeWork = TypeOfChangeWork.PERMANENT
    premium.value = ""
    _transferStart.value = null
    _transferEnd.value = null
    foundation.value = ""
    foundationType.value = FoundationType.CONTRACT
    transferReason.value = ""
  }

  fun getT5() = T5(
    dataCreated = Calendar.getInstance().time,
    employer = _employer.value!!,
    organization = _organization,
    number = number,

    transferReason = transferReason.value ?: "",
    oldDivision = oldDivision,
    oldPosition = oldPosition,
    newDivision = _selectedDivision,
    newPosition = _selectedPosition,
    typeOfChangeWork = typeOfChangeWork,
    premium = premium.value!!.toDouble(),

    transferStart = _transferStart.value,
    transferEnd = if (typeOfChangeWork == TypeOfChangeWork.TEMPORARY) _transferEnd.value else null,

    foundation = if (_foundationType.value == FoundationType.OTHER_DOC) foundation.value
      ?: "" else "",

    contractData = if (_foundationType.value == FoundationType.CONTRACT) _employer.value?.T1Local?.contractData else null,
    contractNumber = if (_foundationType.value == FoundationType.CONTRACT) _employer.value?.T1Local?.contractNumber
      ?: "" else ""
  )

  fun saveT5(t5: T5, uri: Uri) {
    viewModelScope.launch {
      // load file to server
      _error.value = DocumentsRepository.setFile(_organization!!.id, uri) { fileUri ->

        // set file uri to t5
        t5.fileUrl = fileUri.toString()

        // save t5 to server
        _error.value = DocumentsRepository.setT5(t5) {
          removeWork(changeWorkWork)
          _externalAction.value = Action.AFTER_CHANGE_WORK
        }
      }
    }
  }

  private val _editDate = MutableLiveData<DateType?>(null)
  fun setEditTime(type: DateType) {
    _editDate.value = type
  }

  fun saveDate(date: Date) {
    when (_editDate.value) {
      DateType.START_TRANSFER -> transferStart
      DateType.END_TRANSFER -> transferEnd
      else -> return
    }.value = date
  }
}