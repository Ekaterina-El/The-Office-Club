package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.Address

data class Organization(
  var id: String = "",
  var fullName: String = "",
  var shortName: String = "",
  var okpo: String = "",

  var address: Address = Address(),

  var organizationHeadId: String = "",
  var organizationHeadLocal: User? = null,

  var humanResourcesDepartmentHeadId: String = "",
  var organizationHumanResourcesDepartmentHeadLocal: User? = null,

  val editors: List<String> = listOf(),
  var divisionsId: List<String> = listOf(),

  val employees: List<String> = listOf(),
  val positions: List<String> = listOf(),

  val lastTableNumber: Int = 0,
  val lastOrderNumber: Int = 0
): java.io.Serializable