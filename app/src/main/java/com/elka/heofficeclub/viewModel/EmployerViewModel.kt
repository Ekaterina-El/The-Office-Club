package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.EmployeesRepository
import com.elka.heofficeclub.service.repository.OrganizationRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class EmployerViewModel(application: Application) : BaseViewModel(application) {
  // region Screens
  protected fun goBack() {
    _externalAction.value = Action.GO_BACK
  }

  val screens: Int = 6
  var lastScreen = 0
  private val _screen = MutableLiveData(1)
  val screen get() = _screen

  fun goNextScreen() = changeScreen(next = true)
  fun goPrevScreen() = changeScreen(next = false)


  private fun onEndScreens() {
    if (isCreation.value!!) {
      Log.d("onEndScreens", "create")
      toCreateEmployer()
    } else {
      Log.d("onEndScreens", "save")
    }
  }

  private fun toCreateEmployer() {
    val work = Work.CREATE_EMPLOYER
    addWork(work)

    viewModelScope.launch {
      // get employer table number
      _error.value = OrganizationRepository.regNextTableNumber(organization!!.id) { tableNumber ->

        // create user
        val employer =
          Employer(tableNumber = tableNumber, divisionId = "", organizationId = organization!!.id)
        _error.value = EmployeesRepository.createEmployer(employer) { newEmployer ->

          // register T2 doc number
          _error.value =
            OrganizationRepository.regNextOrderNumber(organization!!.id) { orderNumber ->

              // create T2
              val t2 = newT2
              t2.number = orderNumber
              t2.dataCreated = Calendar.getInstance().time
              t2.orgId = organization!!.id
              t2.orgName = organization!!.fullName
              t2.codeOKPO = organization!!.okpo
              t2.tableNumber = newEmployer.tableNumber
              t2.alphabet = t2.lastName[0].toString()


              _error.value = DocumentsRepository.setT2(t2) { t2End ->

                createdT2 = t2End

                // register T1 doc number
                _error.value =
                  OrganizationRepository.regNextOrderNumber(organization!!.id) { t1OrderNumber ->
                    val premValue = if (premium.value!! == "") 0.0 else premium.value!!.toDouble()
                    val premium = (premValue * 100).roundToInt().toDouble() / 100

                    newT1 = T1(
                      number = t1OrderNumber,
                      orgId = organization!!.id,
                      orgName = organization!!.fullName,
                      codeOKPO = organization!!.okpo,
                      dataCreated = getCurrentTime(),
                      fullName = t2.fullName,
                      employerTableNumber = t2.tableNumber,

                      position = _selectedPosition,
                      division = selectedDivision,
                      conditionOfWork = conditionOfWork.value!!,
                      natureOfWork = natureOfWork.value!!,
                      premium = premium,
                      trialPeriod = trialPeriod.value!!.toIntOrEmpty(),
                      contractData = _contractDate.value,
                      contractNumber = contractNumber.value!!,

                      hiredFrom = _hiredFrom.value,
                      hiredBy = _hiredBy.value,
                    )
                    _externalAction.value = Action.CREATE_FILE_T1
                  }

              }
            }
        }
      }
    }

  }

  private fun changeScreen(next: Boolean) {
    if (next && !checkFieldsCurrentScreen()) return



    lastScreen = _screen.value!!
    val newScreenValue = if (next) lastScreen + 1 else lastScreen - 1

    when {
      newScreenValue < 1 -> goBack()
      newScreenValue > screens -> onEndScreens()
      else -> _screen.value = newScreenValue
    }
  }
  // endregion

  var newT1: T1? = null
  var createdT2: T2? = null

  // region Screen 1
  val INN = MutableLiveData("")
  val SNILS = MutableLiveData("")
  val fullName = MutableLiveData("")

  protected val _birthdate = MutableLiveData<Date?>(null)
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

  protected val _passportDate = MutableLiveData<Date?>(null)
  val passportDate get() = _passportDate

  protected val _dateOfRegAccorinigAddress = MutableLiveData<Date?>(null)
  val dateOfRegAccorinigAddress get() = _dateOfRegAccorinigAddress

  protected var _gender = Gender.M
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

  protected var _educationType: EducationType? = null
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


  protected var _postgEducationType: PostgraduateVocationalEducationType? = null
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

  // region Family
  protected var _marriedStatus: MaritalStatus? = null
  fun setMarriedStatus(maritalStatus: MaritalStatus) {
    _marriedStatus = maritalStatus
  }

  protected var members: List<Member> = listOf()
  fun setFamilyMembers(members: List<Member>) {
    this.members = members
  }

  protected val _socialBenefits = MutableLiveData<List<SocialBenefit>>()
  val socialBenefits get() = _socialBenefits

  protected val _works = MutableLiveData<List<WorkExperience>>()
  val works get() = _works

  protected val _attestation = MutableLiveData<List<Attestation>>()
  val attestation get() = _attestation

  protected val _advanceTraining = MutableLiveData<List<AdvanceTraining>>()
  val advanceTraining get() = _advanceTraining

  protected val _profTraining = MutableLiveData<List<ProfTraining>>()
  val profTraining get() = _profTraining

  protected val _gifts = MutableLiveData<List<Gift>>()
  val gifts get() = _gifts

  protected val _vocations = MutableLiveData<List<Vacation>>()
  val vocations get() = _vocations
  // endregion

  val moreInform = MutableLiveData("")

  // region Work
  val conditionOfWork = MutableLiveData("")
  val natureOfWork = MutableLiveData("")
  val premium = MutableLiveData("")
  val trialPeriod = MutableLiveData("")
  val contractNumber = MutableLiveData("")

  protected val _contractDate = MutableLiveData<Date?>(null)
  val contractDate get() = _contractDate

  protected val _hiredFrom = MutableLiveData<Date?>(null)
  val hiredFrom get() = _hiredFrom

  protected val _hiredBy = MutableLiveData<Date?>(null)
  val hiredBy get() = _hiredBy


  protected val _positions = MutableLiveData<List<OrganizationPosition>>(listOf())
  val positions get() = _positions
  fun setPositions(value: List<OrganizationPosition>) {
    _positions.value = value
  }


  protected var _selectedPosition: OrganizationPosition? = null
  val selectedPosition get() = _selectedPosition
  fun selectPosition(organizationPosition: OrganizationPosition) {
    _selectedPosition = organizationPosition
  }

  private val _divisions = MutableLiveData<List<Division>>()
  val divisions get() = _divisions
  fun setDivisions(value: List<Division>) {
    _divisions.value = value
  }

  private var _selectedDivision: Division? = null
  val selectedDivision get() = _selectedDivision
  fun selectDivision(division: Division) {
    _selectedDivision = division
  }

  private var _organization: Organization? = null
  val organization get() = _organization
  fun setOrganization(value: Organization) {
    _organization = value
  }
  // endregion

  // region Check fields
  private val _fieldErrors = MutableLiveData<List<FieldError>>()
  val fieldErrors get() = _fieldErrors

  private fun checkFieldsCurrentScreen(): Boolean {
    val errors: List<FieldError> = when (_screen.value) {
      1 -> checkFieldsScreen(fields1)
      6 -> checkFieldsScreen(fields6)
      else -> listOf()
    }
    _fieldErrors.value = errors

    return errors.isEmpty()
  }

  private val fields1 = hashMapOf(
    Pair(Field.INN, INN),
    Pair(Field.SNILS, SNILS),
    Pair(Field.EMPLOYER_FULL_NAME, fullName),
    Pair(Field.EMPLOYER_BIRTHDATE, birthdate),
    Pair(Field.EMPLOYER_BIRTHPLACE, birthplace),
    Pair(Field.EMPLOYER_BIRTHPLACE_CODE, birthplaceCode),
    Pair(Field.EMPLOYER_PHONE_NUMBER, phoneNumber),
    Pair(Field.EMPLOYER_ADDR_BY_PASS, addrByPass),
    Pair(Field.EMPLOYER_ADDR_BY_PASS_POSTCODE, addrByPassPostcode),
    Pair(Field.EMPLOYER_ADDR_BY_FACT, addrInFact),
    Pair(Field.EMPLOYER_ADDR_BY_FACT_POSTCODE, addressInFactPostcode),
    Pair(Field.EMPLOYER_DATE_OF_REG_ADDR, _dateOfRegAccorinigAddress),
    Pair(Field.EMPLOYER_PASS_NUMBER, passportNumber),
    Pair(Field.EMPLOYER_PASS_SERIAL, passportSerial),
    Pair(Field.EMPLOYER_PASS_DATE, _passportDate),
    Pair(Field.EMPLOYER_PASS_ORG, passportOrganization),
  )

  private val fields6 = hashMapOf(
    Pair(Field.CONTRACT_DATE, _contractDate),
    Pair(Field.CONTRACT_NUMBER, contractNumber),
  )

  private fun checkFieldsScreen(fields: HashMap<Field, out MutableLiveData<out Any?>>): List<FieldError> {
    val errors = arrayListOf<FieldError>()

    for (field in fields) {
      val value = field.value.value
      var hasError = value == null
      if (!hasError && value is String) hasError = value.isEmpty()
      if (hasError) errors.add(FieldError(field.key, FieldErrorType.IS_REQUIRE))
    }

    return errors
  }
  // endregion

  fun clear() {
    _screen.value = 1
    _externalAction.value = null
    clearWork()
    setEmployer(null)

    createdT2 = null
    newT1 = null
  }

  private val fullNameParts: List<String>
    get() {
      val fullNameParts = fullName.value!!.split(" ")
      val lastName = fullNameParts.getOrNull(0) ?: ""
      val firstName = fullNameParts.getOrNull(1) ?: ""
      val patronymic = fullNameParts.getOrNull(2) ?: ""
      return listOf(lastName, firstName, patronymic)
    }

  private val lastName get() = fullNameParts[0]
  private val firstName get() = fullNameParts[1]
  private val patronymic get() = fullNameParts[2]

  private val firstLang
    get() = Lang(
      name = firstLangName.value!!, level = firstLangLevel.value!!, code = firstLangCode.value!!
    )

  private val secondLang
    get() = Lang(
      name = secondLangName.value!!, level = secondLangLevel.value!!, code = secondLangCode.value!!
    )

  private val firstEducation
    get() = Education(
      institute = institute1.value!!,
      documentOfEducation = educationDoc1.value!!,
      yearOfGraduation = education1YearOfEnd.value!!,
      qualification = education1Qualification.value!!,
      directionCode = education1SpecialtyСode.value!!,
      serial = education1Serial.value!!,
      number = education1Number.value!!
    )

  private val secondEducation
    get() = Education(
      institute = institute2.value!!,
      documentOfEducation = educationDoc2.value!!,
      yearOfGraduation = education2YearOfEnd.value!!,
      qualification = education2Qualification.value!!,
      directionCode = education2SpecialtyСode.value!!,
      serial = education2Serial.value!!,
      number = education2Number.value!!
    )

  private val postgEducation
    get() = Education(
      institute = postgInstitute.value!!,
      documentOfEducation = postgEducationDoc.value!!,
      yearOfGraduation = postgEducationYearOfEnd.value!!,
      qualification = postgEducationQualification.value!!,
      directionCode = postgEducationSpecialtyСode.value!!,
      serial = postgEducationSerial.value!!,
      number = postgEducationNumber.value!!
    )

  private val militaryRegistration
    get() = MilitaryRegistration(
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

  private val lengthOfService
    get() = LengthOfService(
      total = Service(
        years = totalY.value!!.toIntOrEmpty(),
        months = totalM.value!!.toIntOrEmpty(),
        days = totalD.value!!.toIntOrEmpty()
      ),
      continuous = Service(
        years = continuousY.value!!.toIntOrEmpty(),
        months = continuousM.value!!.toIntOrEmpty(),
        days = continuousD.value!!.toIntOrEmpty()
      ),
      toBonus = Service(
        years = toBonusY.value!!.toIntOrEmpty(),
        months = toBonusM.value!!.toIntOrEmpty(),
        days = toBonusD.value!!.toIntOrEmpty()
      ),
    )

  protected val newT2
    get() = (employer.value?.T2Local ?: T2()).copy(
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
      moreInform = moreInform.value!!,

      contractData = _contractDate.value,
      contractNumber = contractNumber.value!!,
    )

  fun addPosition(organizationPosition: OrganizationPosition) {
    val items = positions.value!!.toMutableList()
    items.add(organizationPosition)
    _positions.value = items
  }

  fun saveT1(t1: T1, uri: Uri) {
    viewModelScope.launch {
      // save file to server by uri
      _error.value = DocumentsRepository.setFile(_organization!!.id, uri) { url ->

        // set file uri to t1
        t1.fileUrl = url.toString()

        // save t1 to server
        _error.value = DocumentsRepository.setT1(t1) {
          // change t2 from server
          _error.value = DocumentsRepository.changeT2AfterT1(t1, createdT2!!)
        }
      }

      val work = Work.CREATE_EMPLOYER
      removeWork(work)

      goBack()
    }
  }

  // region Work with dates
  private val _editDate = MutableLiveData<DateType?>(null)
  fun setEditTime(type: DateType) {
    _editDate.value = type
  }

  fun saveDate(date: Date) {
    when (_editDate.value) {
      DateType.BIRTDAY -> _birthdate
      DateType.PASSPORT_DATE -> _passportDate
      DateType.CONTRACT -> _contractDate
      DateType.REG_ACCORINING_ADDRESS -> _dateOfRegAccorinigAddress
      DateType.START_WORK -> hiredFrom
      DateType.END_WORK -> hiredBy
      else -> return
    }.value = date
  }

  // endregion

  private val _employer = MutableLiveData<Employer?>(null)
  val employer get() = _employer

  fun setEmployer(employer: Employer?) {
    _employer.value = employer

    val T2 = employer?.T2Local ?: T2()
    INN.value = T2.INN
    SNILS.value = T2.SNILS
    fullName.value = T2.fullName
    _gender = T2.gender

    _birthdate.value = T2.birthdate
    birthplace.value = T2.birthplaceName
    birthplaceCode.value = T2.birthplaceCode

    nationality.value = T2.citizenshipName
    nationalityCode.value = T2.citizenshipCode
    phoneNumber.value = T2.phoneNumber

    addrByPass.value = T2.addressOfResidenceAccordingPassport
    addrByPassPostcode.value = T2.addressOfResidenceAccordingPassportPostCode

    addrInFact.value = T2.addressOfResidenceAccordingInFact
    addressInFactPostcode.value = T2.addressOfResidenceAccordingInFactPostCode

    passportNumber.value = T2.passportNumber
    passportSerial.value = T2.passportSerial
    passportOrganization.value = T2.passportGivenBy
    _passportDate.value = T2.passportDateOfGiven
    _dateOfRegAccorinigAddress.value = T2.dateOfRegAccordingAddress

    val firstLang = T2.firstLang ?: Lang()
    firstLangName.value = firstLang.name
    firstLangLevel.value = firstLang.level
    firstLangCode.value = firstLang.code

    val secondLang = T2.secondLang ?: Lang()
    secondLangName.value = secondLang.name
    secondLangLevel.value = secondLang.level
    secondLangCode.value = secondLang.code

    _educationType = T2.education

    val firstEducation = T2.firstEducation ?: Education()
    institute1.value = firstEducation.institute
    educationDoc1.value = firstEducation.documentOfEducation
    education1Serial.value = firstEducation.serial
    education1Number.value = firstEducation.serial
    education1YearOfEnd.value = firstEducation.yearOfGraduation
    education1Qualification.value = firstEducation.qualification
    education1SpecialtyСode.value = firstEducation.directionCode

    val secondEducation = T2.secondEducation ?: Education()
    institute2.value = secondEducation.institute
    educationDoc2.value = secondEducation.documentOfEducation
    education2Serial.value = secondEducation.serial
    education2Number.value = secondEducation.serial
    education2YearOfEnd.value = secondEducation.yearOfGraduation
    education2Qualification.value = secondEducation.qualification
    education2SpecialtyСode.value = secondEducation.directionCode

    val postgraduateEducation = T2.postgraduateEducation ?: Education()
    postgInstitute.value = postgraduateEducation.institute
    postgEducationDoc.value = postgraduateEducation.documentOfEducation
    postgEducationSerial.value = postgraduateEducation.serial
    postgEducationNumber.value = postgraduateEducation.number
    postgEducationYearOfEnd.value = postgraduateEducation.yearOfGraduation
    postgEducationQualification.value = postgraduateEducation.qualification
    postgEducationSpecialtyСode.value = postgraduateEducation.directionCode

    professionMainName.value = T2.mainProfession
    professionMainCode.value = T2.mainProfessionCode
    professionSecondName.value = T2.secondProfession
    professionSecondCode.value = T2.secondProfessionCode

    members = T2.familyComposition
    works.value = T2.works
    attestation.value = T2.attestation
    advanceTraining.value = T2.advanceTraining
    profTraining.value = T2.profTraining
    gifts.value = T2.gifts
    vocations.value = T2.vocations
    socialBenefits.value = T2.socialBenefits

    val militaryRegistration = T2.militaryRegistration
    reserveСategory.value = militaryRegistration.category
    rank.value = militaryRegistration.rank
    profile.value = militaryRegistration.militaryRegistrySpec
    codeVUS.value = militaryRegistration.codeVUS
    category.value = militaryRegistration.categoryOfFitness
    militaryName.value = militaryRegistration.militaryOfficeName
    militaryRegistryGeneral.value = militaryRegistration.militaryRegistryGeneral
    militaryRegistrySpec.value = militaryRegistration.militaryRegistrySpec
    markOfDeregistration.value = militaryRegistration.markOfDeregistration

    val lengthOfService = T2.lengthOfService
    val total = lengthOfService.total ?: Service()
    totalY.value = total.years.toString()
    totalM.value = total.months.toString()
    totalD.value = total.days.toString()

    val continuous = lengthOfService.continuous ?: Service()
    continuousY.value = continuous.years.toString()
    continuousM.value = continuous.months.toString()
    continuousD.value = continuous.days.toString()

    val toBonus = lengthOfService.toBonus ?: Service()
    toBonusY.value = toBonus.years.toString()
    toBonusM.value = toBonus.months.toString()
    toBonusD.value = toBonus.days.toString()

    moreInform.value = T2.moreInform

    _selectedDivision = employer?.divisionLocal ?: Division(name = "-")
    _selectedPosition = employer?.positionLocal ?: OrganizationPosition(name = "-")

    val T1 = employer?.T1Local ?: T1()
    conditionOfWork.value = T1.conditionOfWork
    natureOfWork.value = T1.natureOfWork
    premium.value = T1.premium.toString()
    trialPeriod.value = T1.trialPeriod.toString()
    contractNumber.value = T1.contractNumber
    _contractDate.value = T1.contractData
    _hiredFrom.value = T1.hiredFrom
    _hiredBy.value = T1.hiredBy
  }

  private val _isCreation = MutableLiveData<Boolean>(false)
  val isCreation get() = _isCreation
  fun setViewStatus(creation: Boolean) {
    _isCreation.value = creation
  }

  fun addVacations(vacations: List<Vacation>) {
    val currVacations = _vocations.value!!.toMutableList()
    currVacations.addAll(vacations)
    _vocations.value = currVacations
  }

  fun addGift(gift: Gift) {
    val currGifts = _gifts.value!!.toMutableList()
    currGifts.add(gift)
    _gifts.value = currGifts
  }

  fun updateWork(t5: T5) {
    val position = t5.newPosition!!
    val division = t5.newDivision!!
    val endTime = t5.transferEnd
    val startTime = t5.transferStart
    val premium = t5.premium

    val employer = employer.value!!

    if (t5.typeOfChangeWork == TypeOfChangeWork.PERMANENT) {
      employer.positionLocal = position
      employer.positionId = position.id
      employer.divisionLocal = division
      employer.divisionId = division.id
      employer.premium = premium

      employer.tempPremium = 0.0
      employer.positionTempId = ""
      employer.positionTempLocal = null
      employer.divisionTempId = ""
      employer.divisionTempLocal = null
      employer.startWorkTmp = null
      employer.endWorkTmp = null
    } else {
      if (endTime!!.time > Calendar.getInstance().time.time) return
      employer.tempPremium = premium
      employer.positionTempId = position.id
      employer.positionTempLocal = position
      employer.divisionTempId = division.id
      employer.divisionTempLocal = division
      employer.startWorkTmp = startTime
      employer.endWorkTmp = endTime
    }

    _employer.value = employer
  }
}