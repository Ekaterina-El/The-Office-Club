package com.elka.heofficeclub.service.model

data class Division(
  var id: String = "",
  val name: String = "",

  var organization: String = "",

  val employees: List<String> = listOf()
)