package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.Gift
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.documents.forms.T11
import com.elka.heofficeclub.service.model.documents.forms.T6
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.ibm.icu.text.RuleBasedNumberFormat
import kotlinx.coroutines.launch
import java.util.*

class GetGiftViewModel(application: Application) : BaseViewModelWithFields(application) {
  val giftDescription = MutableLiveData("")
  val giftType = MutableLiveData("")
  val giftReason = MutableLiveData("")
  val giftSum = MutableLiveData("")

  var gift: Gift? = null

  private val getGiftWork = Work.GET_GIFT

  fun clear() {
    giftDescription.value = ""
    giftType.value = ""
    giftReason.value = ""
    giftSum.value = ""
    employer = null
    organization = null
    gift = null
    _externalAction.value = null
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

    addWork(getGiftWork)

    // generate PDF file
    _externalAction.value = Action.GENERATE_T11
  }
  override val fields = hashMapOf(
    Pair(Field.GIFT_DESCRIPTION, giftDescription as MutableLiveData<Any?>),
    Pair(Field.GIFT_TYPE, giftType as MutableLiveData<Any?>),
    Pair(Field.GIFT_REASON, giftReason as MutableLiveData<Any?>),
  )

  fun getT11() = T11(
    dataCreated = Calendar.getInstance().time,
    employer = employer,
    division = employer?.divisionLocal,
    position = employer?.positionLocal,
    organization = organization,
    description = giftDescription.value!!,
    giftType = giftType.value!!,
    sum = giftSum.value!!.toDouble(),
    reason = giftReason.value!!,

  )

  fun saveT11(t11: T11, uri: Uri) {
    // TODO: get doc number
    // t11.number = number

    /*viewModelScope.launch {
      // load file to server
      _error.value = DocumentsRepository.setFile(organization!!.id, uri) { fileUri ->

        // set file uri to t11
        t11.fileUrl = fileUri.toString()

        // save t6 to server
        _error.value = DocumentsRepository.setT11(t11) { newT11, gift ->
          this@GetGiftViewModel.gift = gift
          removeWork(getGiftWork)
          _externalAction.value = Action.AFTER_GET_GIFT
        }
      }
    }*/
  }
}