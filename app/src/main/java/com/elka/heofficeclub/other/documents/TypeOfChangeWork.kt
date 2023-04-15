package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class TypeOfChangeWork(val str: Int, val text: String) {
  PERMANENT(R.string.permanent, "Постоянный"), TEMPORARY(R.string.temporary, "Временный")
}

fun getTypeOfChangeWorkItems() = TypeOfChangeWork.values().map {
  return@map SpinnerItem(it.str, it)
}