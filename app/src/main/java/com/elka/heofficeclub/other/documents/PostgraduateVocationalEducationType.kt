package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class PostgraduateVocationalEducationType(val code: Int, val res: Int) {
  NONE(0, R.string.none),
  Postgraduate(2, R.string.postgraduate),
  Adjunct(2, R.string.adjunct),
  Doctoral(1, R.string.doctoral),
}

fun getPostgraduateEducationSpinnerItems() = PostgraduateVocationalEducationType.values().map {
  return@map SpinnerItem(it.res, it)
}