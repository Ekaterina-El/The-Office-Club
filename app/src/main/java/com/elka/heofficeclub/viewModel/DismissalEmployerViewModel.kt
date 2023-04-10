package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.FoundationType
import com.elka.heofficeclub.other.documents.TypeOfChangeWork
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.service.model.documents.forms.T8
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.OrganizationRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class DismissalEmployerViewModel(application: Application) : BaseViewModelWithFields(application) {
  private val dismissalEmployerWork = Work.DISMISSAL_EMPLOYER

  private val _dismissalDate = MutableLiveData<Date?>(null)
  val dismissalDate get() = _dismissalDate

  val foundation = MutableLiveData("")
  val foundationDoc = MutableLiveData("")
  val foundationNumber = MutableLiveData("")

  private val _foundationDocDismissalDate = MutableLiveData<Date?>(null)
  val foundationDocDismissalDate get() = _foundationDocDismissalDate

  private var _organization: Organization? = null

  private var _employer = MutableLiveData<Employer?>(null)
  val employer get() = _employer

  fun initData(
    organization: Organization,
    employer: Employer,
  ) {
    _organization = organization
    _employer.value = employer
  }


  private var number = 0
  fun trySave() {
    if (!checkFields()) return

    addWork(dismissalEmployerWork)
    viewModelScope.launch {
      OrganizationRepository.regNextOrderNumber(_organization!!.id) { number ->
        this@DismissalEmployerViewModel.number = number

        // generate PDF file
        _externalAction.value = Action.GENERATE_T8
      }
    }
  }

  override val fields: HashMap<Field, MutableLiveData<Any?>> = hashMapOf(
    Pair(Field.DISMISSAL_DATE, _dismissalDate as MutableLiveData<Any?>),
    Pair(Field.FOUNDATION, foundation as MutableLiveData<Any?>),
    Pair(Field.FOUNDATION_DOC, foundationDoc as MutableLiveData<Any?>),
    Pair(Field.FOUNDATION_NUMBER, foundationNumber as MutableLiveData<Any?>),
    Pair(Field.FOUNDATION_DOC_DATE, _foundationDocDismissalDate as MutableLiveData<Any?>),
  )


  fun clear() {
    _dismissalDate.value = null
    _foundationDocDismissalDate.value = null
    _externalAction.value = null
    _organization = null
    _employer.value = null
  }

  fun getT8() = T8(
    number = number,
    dataCreated = Calendar.getInstance().time,
    employer = _employer.value!!,
    organization = _organization,

    dismissalDate = _dismissalDate.value,
    reason = foundation.value!!,
    reasonDoc = foundationDoc.value!!,
    reasonNumber = foundationNumber.value!!,
    reasonDate = _foundationDocDismissalDate.value
  )

  fun saveT8(t8: T8, uri: Uri) {
    viewModelScope.launch {
      // load file to server
      _error.value = DocumentsRepository.setFile(_organization!!.id, uri) { fileUri ->

        // set file uri to t8
        t8.fileUrl = fileUri.toString()

        // save t8 to server
        _error.value = DocumentsRepository.setT8(t8) {
          removeWork(dismissalEmployerWork)
          _externalAction.value = Action.AFTER_DISMISSAL_WORK
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
      DateType.DISMISSAL_DATE -> dismissalDate
      DateType.FOUNDATION_DOC_DISMISSAL_DATE -> foundationDocDismissalDate
      else -> return
    }.value = date
  }
}