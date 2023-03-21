package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class Gender(val res: Int) { M(R.string.man), W(R.string.woman) }

fun getGenderSpinnerItems() = Gender.values().map {
  return@map SpinnerItem(it.res, it)
}