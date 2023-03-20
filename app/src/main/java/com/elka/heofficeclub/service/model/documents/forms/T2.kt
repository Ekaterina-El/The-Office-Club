package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.other.toDays
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
  var cnils: String = "",
  var natureOfWork: String = "",
  var typeOfWork: String = "",
  var gender: Gender = Gender.M,

  var lastName: String = "",
  var firstName: String = "",
  var patronymic: String = "",

  var birthdate: Date = Date(0),
  var birthplaceName: String = "",
  var birthplaceCode: String = "",

  var citizenshipName: String = "",
  var citizenshipCode: String = "",

  var firstLang: String = "",
  var firstLangCode: String = "",
  var secondLang: String = "",
  var secondLangCode: String = "",

  var education: String = "",
  var educationCode: String = "",
  var firstEducation: Education? = null,
  var secondEducation: Education? = null,

  var postgraduateVocationalEducationType: PostgraduateVocationalEducationType? = null,
  var postgraduateEducation: Education? = null,
  var postgraduateVocationalEducationCode: String = "",

  var mainProfession: String = "",
  var mainProfessionCode: String = "",
  var secondProfession: String = "",
  var secondProfessionCode: String = "",

  var lengthOfService: LengthOfService = LengthOfService(),
  var maritalStatus: MaritalStatus = MaritalStatus.NeverMarried,

  var familyComposition: List<Member> = listOf(),

  var passportNumber: String = "",
  var passportSerial: String = "",
  var passportDateOfGiven: Date = Date(0),
  var passportDateGivenBy: String = "",

  var addressOfResidenceAccordingPassport: String = "",
  var addressOfResidenceAccordingPassportPostCode: String = "",
  var addressOfResidenceAccordingInFacy: String = "",
  var addressOfResidenceAccordingInFacyPostCode: String = "",

  var dateOfRegAccordingAddress: Date = Date(),
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

) : DocForm(fileUrl, number, orgId, orgName, codeOKPO, dataCreated)