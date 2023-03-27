package com.elka.heofficeclub.service.model.documents.forms

import android.net.Uri
import com.elka.heofficeclub.other.documents.FormType
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.User
import java.util.*


data class T1(
  override val type: FormType = FormType.T1,
  override var fileUrl: String? = null,
  override var number: Int = 0,
  override val orgId: String = "",
  override val orgName: String = "",
  override val codeOKPO: String = "",
  override val dataCreated: Date = Calendar.getInstance().time,

  val hiredFrom: Date? = null,
  val hiredBy: Date? = null,

  var fullName: String = "",
  var employerTableNumber: Int = 0,

  var division: Division? = Division(),
  var position: OrganizationPosition? = OrganizationPosition(),

  var premium: Double = 0.0,
  var trialPeriod: Int = 0,

  var contractData: Date? = null,
  var contractNumber: String = "",

  var conditionOfWork: String = "",
  var natureOfWork: String = "",

  var header: User = User()
) : DocForm(fileUrl = fileUrl, number = number, orgId = orgId, orgName = orgName, codeOKPO = codeOKPO, dataCreated = dataCreated, type =  type)

