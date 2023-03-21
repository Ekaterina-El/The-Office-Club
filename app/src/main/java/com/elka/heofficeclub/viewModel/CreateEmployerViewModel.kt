package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Lang
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.other.toIntOrEmpty
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T2
import java.util.*

class CreateEmployerViewModel(application: Application) : BaseViewModel(application) {
  companion object {
    const val SCREENS = 4
  }

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
    } else if (newScreenValue > SCREENS + 1) {
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

  // region Screen 2
  val firstLangName = MutableLiveData("")
  val firstLangLevel = MutableLiveData("")
  val firstLangCode = MutableLiveData("")

  val secondLangName = MutableLiveData("")
  val secondLangLevel = MutableLiveData("")
  val secondLangCode = MutableLiveData("")

  private var _educationType: EducationType? = null
  fun setEducationType(educationType: EducationType) {
    _educationType = educationType
  }


  val institute1 = MutableLiveData("")
  val educationDoc1 = MutableLiveData("")
  val education1Serial = MutableLiveData("")
  val education1Number = MutableLiveData("")
  val education1YearOfEnd = MutableLiveData("")
  val education1Qualification = MutableLiveData("")
  val education1SpecialtyСode = MutableLiveData("")

  val institute2 = MutableLiveData("")
  val educationDoc2 = MutableLiveData("")
  val education2Serial = MutableLiveData("")
  val education2Number = MutableLiveData("")
  val education2YearOfEnd = MutableLiveData("")
  val education2Qualification = MutableLiveData("")
  val education2SpecialtyСode = MutableLiveData("")


  private var _postgEducationType: PostgraduateVocationalEducationType? = null
  fun setPostgEducationType(educationType: PostgraduateVocationalEducationType) {
    _postgEducationType = educationType
  }

  val postgInstitute = MutableLiveData("")
  val postgEducationDoc = MutableLiveData("")
  val postgEducationSerial = MutableLiveData("")
  val postgEducationNumber = MutableLiveData("")
  val postgEducationYearOfEnd = MutableLiveData("")
  val postgEducationQualification = MutableLiveData("")
  val postgEducationSpecialtyСode = MutableLiveData("")

  val professionMainName = MutableLiveData("")
  val professionMainCode = MutableLiveData("")
  val professionSecondName = MutableLiveData("")
  val professionSecondCode = MutableLiveData("")
  // endregion

  // region Family
  private var _marriedStatus: MaritalStatus? = null
  fun setMarriedStatus(maritalStatus: MaritalStatus) {
    _marriedStatus = maritalStatus
  }

  private var members: List<Member> = listOf()
  fun setFamilyMembers(members: List<Member>) {
    this.members = members
  }
  // endregion

  // region Military
  val reserveСategory = MutableLiveData("")
  val rank = MutableLiveData("")
  val profile = MutableLiveData("")
  val codeVUS = MutableLiveData("")
  val category = MutableLiveData("")
  val militaryName = MutableLiveData("")
  val militaryRegistryGeneral = MutableLiveData("")
  val militaryRegistrySpec = MutableLiveData("")
  val markOfDeregistration = MutableLiveData("")
  // endregion

  // region Length of service
  val totalY = MutableLiveData("")
  val totalM = MutableLiveData("")
  val totalD = MutableLiveData("")
  val continuousY = MutableLiveData("")
  val continuousM = MutableLiveData("")
  val continuousD = MutableLiveData("")
  val toBonusY = MutableLiveData("")
  val toBonusM = MutableLiveData("")
  val toBonusD = MutableLiveData("")
  // endregion

  val moreInform = MutableLiveData("")

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

    firstLangName.value = ""
    firstLangLevel.value = ""
    firstLangCode.value = ""

    secondLangName.value = ""
    secondLangLevel.value = ""
    secondLangCode.value = ""

    _educationType = null

    institute1.value = ""
    educationDoc1.value = ""
    education1Serial.value = ""
    education1Number.value = ""
    education1YearOfEnd.value = ""
    education1Qualification.value = ""
    education1SpecialtyСode.value = ""

    institute2.value = ""
    educationDoc2.value = ""
    education2Serial.value = ""
    education2Number.value = ""
    education2YearOfEnd.value = ""
    education2Qualification.value = ""
    education2SpecialtyСode.value = ""

    postgInstitute.value = ""
    postgEducationDoc.value = ""
    postgEducationSerial.value = ""
    postgEducationNumber.value = ""
    postgEducationYearOfEnd.value = ""
    postgEducationQualification.value = ""
    postgEducationSpecialtyСode.value = ""

    professionMainName.value = ""
    professionMainCode.value = ""
    professionSecondName.value = ""
    professionSecondCode.value = ""

    members = listOf()

    reserveСategory.value = ""
    rank.value = ""
    profile.value = ""
    codeVUS.value = ""
    category.value = ""
    militaryName.value = ""
    militaryRegistryGeneral.value = ""
    militaryRegistrySpec.value = ""
    markOfDeregistration.value = ""

    totalY.value = ""
    totalM.value = ""
    totalD.value = ""
    continuousY.value = ""
    continuousM.value = ""
    continuousD.value = ""
    toBonusY.value = ""
    toBonusM.value = ""
    toBonusD.value = ""

    moreInform.value = ""
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

      val firstLang = Lang(
        name = firstLangName.value!!,
        level = firstLangLevel.value!!,
        code = firstLangCode.value!!
      )

      val secondLang = Lang(
        name = firstLangName.value!!,
        level = firstLangLevel.value!!,
        code = firstLangCode.value!!
      )

      val firstEducation = Education(
        institute = institute1.value!!,
        documentOfEducation = educationDoc1.value!!,
        yearOfGraduation = education1YearOfEnd.value!!,
        qualification = education1Qualification.value!!,
        directionCode = education1SpecialtyСode.value!!,
        series = education1Serial.value!!,
        number = education1Number.value!!
      )

      val secondEducation = Education(
        institute = institute2.value!!,
        documentOfEducation = educationDoc2.value!!,
        yearOfGraduation = education2YearOfEnd.value!!,
        qualification = education2Qualification.value!!,
        directionCode = education2SpecialtyСode.value!!,
        series = education2Serial.value!!,
        number = education2Number.value!!
      )

      val postgEducation = Education(
        institute = postgInstitute.value!!,
        documentOfEducation = postgEducationDoc.value!!,
        yearOfGraduation = postgEducationYearOfEnd.value!!,
        qualification = postgEducationQualification.value!!,
        directionCode = postgEducationSpecialtyСode.value!!,
        series = postgEducationSerial.value!!,
        number = postgEducationNumber.value!!
      )

      val militaryRegistration = MilitaryRegistration(
        category = reserveСategory.value!!,
        rank = rank.value!!,
        composition = profile.value!!,
        codeVUS = codeVUS.value!!,
        categoryOfFitness = category.value!!,
        militaryOfficeName = militaryName.value!!,
        militaryRegistryGeneral = militaryRegistryGeneral.value!!,
        militaryRegistrySpec = militaryRegistrySpec.value!!,
        markOfDeregistration = markOfDeregistration.value!!
      )

      val lengthOfService =  LengthOfService(
        total = Service(years = totalY.value!!.toIntOrEmpty(), months = totalM.value!!.toIntOrEmpty(), days = totalD.value!!.toIntOrEmpty()),
        continuous = Service(years = continuousY.value!!.toIntOrEmpty(), months = continuousM.value!!.toIntOrEmpty(), days = continuousD.value!!.toIntOrEmpty()),
        toBonus = Service(years = toBonusY.value!!.toIntOrEmpty(), months = toBonusM.value!!.toIntOrEmpty(), days = toBonusD.value!!.toIntOrEmpty()),
      )


      return T2(
        INN = INN.value!!,
        SNILS = SNILS.value!!,

        gender = _gender,

        lastName = lastName,
        firstName = firstName,
        patronymic = patronymic,

        birthdate = _birthdate.value,
        birthplaceName = birthplace.value!!,
        birthplaceCode = birthplaceCode.value!!,

        citizenshipName = nationality.value!!,
        citizenshipCode = nationalityCode.value!!,

        phoneNumber = phoneNumber.value!!,

        addressOfResidenceAccordingPassport = addrByPass.value!!,
        addressOfResidenceAccordingPassportPostCode = addrByPassPostcode.value!!,

        addressOfResidenceAccordingInFact = addrInFact.value!!,
        addressOfResidenceAccordingInFactPostCode = addressInFactPostcode.value!!,

        dateOfRegAccordingAddress = _dateOfRegAccorinigAddress.value,

        passportNumber = passportNumber.value!!,
        passportSerial = passportSerial.value!!,
        passportDateOfGiven = _passportDate.value,
        passportGivenBy = passportOrganization.value!!,

        // Education
        firstLang = firstLang,
        secondLang = secondLang,
        education = _educationType,
        firstEducation = firstEducation,
        secondEducation = secondEducation,
        postgraduateVocationalEducationType = _postgEducationType,
        postgraduateEducation = postgEducation,

        mainProfession = professionMainName.value!!,
        mainProfessionCode = professionMainCode.value!!,

        secondProfession = professionSecondName.value!!,
        secondProfessionCode = professionSecondCode.value!!,

        // Family
        maritalStatus = _marriedStatus,
        familyComposition = members,

        militaryRegistration = militaryRegistration,
        lengthOfService = lengthOfService,
        moreInform = moreInform.value!!
      )
    }

  private fun toCreateFile() {
    _externalAction.value = Action.CREATE_FILE
    val a = newEmployer
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

