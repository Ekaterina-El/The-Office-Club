package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class MaritalStatus(val code: Int, val res: Int) {
  NeverMarried(1, R.string.NeverMarried),
  IsInARegisteredMarriage(2, R.string.IsInARegisteredMarriage),
  IsInAUnregisteredMarriage(3, R.string.IsInAUnregisteredMarriage),
  WidowerWidow(4, R.string.WidowerWidow),
  OfficiallyDivorced(5, R.string.OfficiallyDivorced),
  Separated(6, R.string.Separated),
}

fun getMarriedStatusSpinnerItems() = MaritalStatus.values().map {
  return@map SpinnerItem(it.res, it)
}
