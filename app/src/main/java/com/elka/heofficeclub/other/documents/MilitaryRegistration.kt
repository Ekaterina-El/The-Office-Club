package com.elka.heofficeclub.other.documents

data class MilitaryRegistration(
  val category: String = "",
  val rank: String = "",
  val composition: String = "",
  val codeVUS: String = "",
  val categoryOfFitness: String = "",
  val militaryOfficeName: String = "",
  val militaryRegistryGeneral: String = "",
  val militaryRegistrySpec: String = "",
  val markOfDeregistration: String = "",
): java.io.Serializable