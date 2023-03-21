package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.service.model.documents.forms.T2

data class Employer(
  var id: String = "",
  val tableNumber: Int = 0,

  var docs: List<String> = listOf(),

  var T2: String = "",
  var T2Local: T2? = null,

  var divisionId: String = "",
  var organizationId: String = "",

  var positionId: String = "",
  val positionLocal: OrganizationPosition? = null
)