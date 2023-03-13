package com.elka.heofficeclub.service.model

data class OrganizationPosition(
  var id: String = "",
  var name: String = "",
  var salary: Double = 0.0,
)

fun List<OrganizationPosition>.filterBy(search: String) =
  this.filter { it.name.contains(search, ignoreCase = true) }