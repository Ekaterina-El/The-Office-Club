package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class EducationType(val code: Int, val res: Int) {
  NONE(0, R.string.none),
  SecondaryCompleteGeneral(7, R.string.SecondaryCompleteGeneral),
  InitialVocational(10, R.string.InitialVocational),
  SecondaryVocational(11, R.string.SecondaryVocational),
  Higher(18, R.string.Higher),
}

fun getEducationSpinnerItems() = EducationType.values().map {
  return@map SpinnerItem(it.res, it)
}


