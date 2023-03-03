package com.elka.heofficeclub.service.model

data class Division(
  val id: String = "",
  val level: Int = 1,
  val name: String = "",
  val employees: List<String> = listOf()
)