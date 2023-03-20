package com.elka.heofficeclub.service.model.documents.forms

import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import java.util.*

class T8(
  override val fileUrl: String = "",
  override var number: String = "",
  override val orgId: String = "",
  override val orgName: String = "",
  override val codeOKPO: String = "",
  override val dataCreated: Date = Calendar.getInstance().time,

  var contractData: Date? = null,
  var contractNumber: String = "",

  var fireDate: Date? = null,

  var fullName: String = "",
  var employerTableNumber: String = "",

  var division: Division? = Division(),
  var position: OrganizationPosition? = OrganizationPosition(),

  var reason: String = "",
  ): DocForm(fileUrl, number, orgId, orgName, codeOKPO, dataCreated)