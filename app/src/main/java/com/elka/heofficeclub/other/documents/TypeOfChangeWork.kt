package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class TypeOfChangeWork(val str: Int) {
  PERMANENT(R.string.permanent), TEMPORARY(R.string.temporary)
}

fun getTypeOfChangeWorkItems() = TypeOfChangeWork.values().map {
  return@map SpinnerItem(it.str, it)
}