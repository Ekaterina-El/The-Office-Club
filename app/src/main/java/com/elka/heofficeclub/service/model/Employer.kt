package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.service.model.documents.forms.T2

data class Employer(
  var id: String = "",
  val tableNumber: Int = 0,

  var docs: List<String> = listOf(),

  var T2: String = "",
  var T2Local: T2? = null,

  var divisionId: String = "",
  var divisionLocal: Division? = null,

  var organizationId: String = "",

  var positionId: String = "",
  var positionLocal: OrganizationPosition? = null
): java.io.Serializable


fun List<Employer>.filterBy(search: String) =
  this.filter {
    it.tableNumber.toString().contains(search, ignoreCase = true) ||
    it.T2Local?.fullName?.contains(search, ignoreCase = true) ?: false ||
    it.positionLocal?.name?.contains(search, ignoreCase = true) ?: false ||
    it.divisionLocal?.name?.contains(search, ignoreCase = true) ?: false
  }

/*
tableNumber
works
attestation
advanceTraining
profTraining
gifts
vocations
socialBenefits


fullName
gender,
birthdate (birthplaceName - birthplaceCode)
citizenshipName (citizenshipCode)

division, position
contract number, contract date

inn
snils

firstLang
secondLang

educationType
firstEducation
secondEducation

postgraduateVocationalEducationType
postgraduateEducation

mainProfession
mainProfessionCode

secondProfession
secondProfessionCode

lengthOfService

maritalStatus
familyComposition

passportNumber
passportSerial
passportDateOfGiven
passportGivenBy

addressOfResidenceAccordingPassport
addressOfResidenceAccordingPassportPostCode

addressOfResidenceAccordingInFact
addressOfResidenceAccordingInFactPostCode
dateOfRegAccordingAddress
phoneNumber

militaryRegistration

moreInform
 */