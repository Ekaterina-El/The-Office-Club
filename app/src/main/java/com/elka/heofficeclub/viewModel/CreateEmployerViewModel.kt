package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.Gender
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T2
import java.util.*

class CreateEmployerViewModel(application: Application) : BaseViewModel(application) {
  // region Screen
  private val _screen = MutableLiveData(1)
  val screen get() = _screen

  fun goNextScreen() = changeScreen(next = true)
  fun goPrevScreen() = changeScreen(next = false)


  private fun changeScreen(next: Boolean) {
    if (next && !checkFieldsCurrentScreen()) return

    val screen = _screen.value!!
    val newScreenValue = if (next) screen + 1 else screen - 1

    if (newScreenValue < 1) {
      goBack()
    } else if (newScreenValue > 4) {
      toCreateFile()
    } else {
      _screen.value = newScreenValue
    }
  }
  // endregion

  // region screen 1
  val INN = MutableLiveData("")
  val SNILS = MutableLiveData("")
  val fullName = MutableLiveData("")

  private val _birthdate = MutableLiveData<Date?>(null)
  val birthdate get() = _birthdate

  val birthplace = MutableLiveData("")
  val birthplaceCode = MutableLiveData("")
  val nationality = MutableLiveData("")
  val nationalityCode = MutableLiveData("")
  val phoneNumber = MutableLiveData("")

  val addrByPass = MutableLiveData("")
  val addrByPassPostcode = MutableLiveData("")

  val addrInFact = MutableLiveData("")
  val addressInFactPostcode = MutableLiveData("")

  val passportNumber = MutableLiveData("")
  val passportSerial = MutableLiveData("")
  val passportOrganization = MutableLiveData("")

  private val _passportDate = MutableLiveData<Date?>(null)
  val passportDate get() = _passportDate

  private val _dateOfRegAccorinigAddress = MutableLiveData<Date?>(null)
  val dateOfRegAccorinigAddress get() = _dateOfRegAccorinigAddress

  private var _gender = Gender.M
  fun setGender(newGender: Gender) {
    _gender = newGender
  }
  // endregion

  fun clear() {
    _screen.value = 1
    _externalAction.value = null

    INN.value = ""
    SNILS.value = ""
    fullName.value = ""

    _birthdate.value = null
    birthplace.value = ""
    birthplaceCode.value = ""
    nationality.value = ""
    nationalityCode.value = ""
    phoneNumber.value = ""

    addrByPass.value = ""
    addrByPassPostcode.value = ""
    addrInFact.value = ""
    addressInFactPostcode.value = ""
    _dateOfRegAccorinigAddress.value = null

    passportNumber.value = ""
    passportSerial.value = ""
    passportOrganization.value = ""
    _passportDate.value = null
  }

  private fun goBack() {
    _externalAction.value = Action.GO_BACK
  }

  private val newEmployer: T2
    get() {
      val fullNameParts = fullName.value!!.split(" ")
      val lastName = fullNameParts.getOrNull(0) ?: ""
      val firstName = fullNameParts.getOrNull(1) ?: ""
      val patronymic = fullNameParts.getOrNull(2) ?: ""

      return T2(
        INN = INN.value!!,
        SNILS = SNILS.value!!,

        gender = _gender,

        lastName = lastName,
        firstName = firstName,
        patronymic = patronymic,

        birthdate = _birthdate.value!!,
        birthplaceName = birthplace.value!!,
        birthplaceCode = birthplaceCode.value!!,

        citizenshipName = nationality.value!!,
        citizenshipCode = nationalityCode.value!!,

        phoneNumber = phoneNumber.value!!,

        addressOfResidenceAccordingPassport = addrByPass.value!!,
        addressOfResidenceAccordingPassportPostCode = addrByPassPostcode.value!!,

        addressOfResidenceAccordingInFact = addrInFact.value!!,
        addressOfResidenceAccordingInFactPostCode = addressInFactPostcode.value!!,

        dateOfRegAccordingAddress = _dateOfRegAccorinigAddress.value!!,

        passportNumber = passportNumber.value!!,
        passportSerial = passportSerial.value!!,
        passportDateOfGiven = _passportDate.value!!,
        passportGivenBy = passportOrganization.value!!,
      )
    }

  private fun toCreateFile() {
    _externalAction.value = Action.CREATE_FILE

    Log.d("toCreateFile", newEmployer.toString())
  }

  private fun checkFieldsCurrentScreen(): Boolean {
    return true
  }

  fun setPositions(value: List<OrganizationPosition>) {

  }

  fun setDivisions(value: List<Division>) {

  }

  fun setOrganization(value: Organization) {

  }

  private val _editDate = MutableLiveData<DateType?>(null)
  fun setEditTime(type: DateType) {
    _editDate.value = type
  }

  fun saveDate(date: Date) {
    when (_editDate.value) {
      DateType.BIRTDAY -> _birthdate
      DateType.PASSPORT_DATE -> _passportDate
      DateType.REG_ACCORINING_ADDRESS -> _dateOfRegAccorinigAddress
      else -> return
    }.value = date
  }

}