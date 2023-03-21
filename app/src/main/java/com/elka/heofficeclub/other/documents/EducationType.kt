package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R

enum class EducationType(val code: Int, var strRes: Int) {
  SecondaryCompleteGeneral(7, R.string.SecondaryCompleteGeneral),
  InitialVocational(10, R.string.InitialVocational),
  SecondaryVocational(11, R.string.SecondaryVocational),
  Higher(18, R.string.Higher),
}