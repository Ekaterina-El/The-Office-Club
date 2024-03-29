package com.elka.heofficeclub.service.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Division(
  var id: String = "",
  val name: String = "",
  val code: String = "",

  var organization: String = "",

  val employees: List<String> = listOf(),
  val docs: List<String> = listOf()
): java.io.Serializable, Parcelable

fun List<Division>.filterBy(filter: String) =
  this.filter { it.name.contains(filter, ignoreCase = true) }

fun List<Division>.getDivisionById(id: String) = this.first { it.id == id }