package com.elka.heofficeclub.service.model

data class Division(
  var id: String = "",
  val level: Int = 1,
  val name: String = "",

  val divisionParentId: String = "",
  var organization: String = "",

  val employees: List<String> = listOf()
)