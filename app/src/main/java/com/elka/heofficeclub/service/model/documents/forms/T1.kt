package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.User
import java.util.*


data class T1(
  override val fileUrl: String = "",
  override var number: String = "",
  override val orgId: String = "",
  override val orgName: String = "",
  override val codeOKPO: String = "",
  override val dataCreated: Date = Calendar.getInstance().time,

  val hiredFrom: Date? = null,
  val hiredBy: Date? = null,

  var fullName: String = "",
  var employerTableNumber: String = "",

  var division: Division? = Division(),
  var position: OrganizationPosition? = OrganizationPosition(),

  var premium: Double = 0.0,
  var trialPeriod: Int = 0,

  var contractData: Date? = null,
  var contractNumber: String = "",

  var header: User = User()
) : DocForm(fileUrl, number, orgId, orgName, codeOKPO, dataCreated)

