package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.Lang
import com.elka.heofficeclub.other.documents.*
import java.util.*

data class T2(
  override val fileUrl: String = "",
  override var number: String = "",
  override val dataCreated: Date = Calendar.getInstance().time,

  override val orgId: String = "",
  override val orgName: String = "",
  override val codeOKPO: String = "",

  var tableNumber: String = "",
  var alphabet: String = "",

  var INN: String = "",
  var SNILS: String = "",

  var natureOfWork: String = "",
  var typeOfWork: String = "",

  var gender: Gender = Gender.M,

  var lastName: String = "",
  var firstName: String = "",
  var patronymic: String = "",

  var birthdate: Date? = Date(0),
  var birthplaceName: String = "",
  var birthplaceCode: String = "",

  var citizenshipName: String = "",
  var citizenshipCode: String = "",

  var firstLang: Lang? = null,
  var secondLang: Lang? = null,

  var education: EducationType? = null,
  var firstEducation: Education? = null,
  var secondEducation: Education? = null,

  var postgraduateVocationalEducationType: PostgraduateVocationalEducationType? = null,
  var postgraduateEducation: Education? = null,

  var mainProfession: String = "",
  var mainProfessionCode: String = "",
  var secondProfession: String = "",
  var secondProfessionCode: String = "",

  var lengthOfService: LengthOfService = LengthOfService(),
  var maritalStatus: MaritalStatus? = MaritalStatus.NeverMarried,

  var familyComposition: List<Member> = listOf(),

  var passportNumber: String = "",
  var passportSerial: String = "",
  var passportDateOfGiven: Date? = Date(0),
  var passportGivenBy: String = "",

  var addressOfResidenceAccordingPassport: String = "",
  var addressOfResidenceAccordingPassportPostCode: String = "",
  var addressOfResidenceAccordingInFact: String = "",
  var addressOfResidenceAccordingInFactPostCode: String = "",

  var dateOfRegAccordingAddress: Date? = Date(),
  var phoneNumber: String = "",

  var militaryRegistration: MilitaryRegistration = MilitaryRegistration(),

  var works: List<WorkExperience> = listOf(),
  var attestation: List<Attestation> = listOf(),
  var advanceTraining: List<AdvanceTraining> = listOf(),
  var profTraining: List<ProfTraining> = listOf(),
  var gifts: List<Gift> = listOf(),
  var vocations: List<Vacation> = listOf(),
  var socialBenefits: List<SocialBenefit> = listOf(),

  var moreInform: String = ""

) : DocForm(fileUrl, number, orgId, orgName, codeOKPO, dataCreated) {
  override fun toString(): String {
    return "inn: $INN; snils: $SNILS; gender: $gender; lastName: $lastName; firstName: $firstName; patronymic: $patronymic;  " +
        "birthdate: $birthdate; birthplaceName: $birthplaceName; birthplaceCode: $birthplaceCode; citizenshipName: $citizenshipName; citizenshipCode: $citizenshipCode;  " +
        "phoneNumber: $phoneNumber; addressOfResidenceAccordingPassport: $addressOfResidenceAccordingPassport; addressOfResidenceAccordingPassportPostCode: $addressOfResidenceAccordingPassportPostCode; addressOfResidenceAccordingInFact: $addressOfResidenceAccordingInFact; addressOfResidenceAccordingInFactPostCode: $addressOfResidenceAccordingInFactPostCode;  " +
        "dateOfRegAccordingAddress: $dateOfRegAccordingAddress; passportNumber: $passportNumber; passportSerial: $passportSerial; passportDateOfGiven: $passportDateOfGiven; passportGivenBy: $passportGivenBy;  "

  }
}