package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.SpinnerItem

enum class FoundationType(val str: Int) {
  CONTRACT(R.string.contract), OTHER_DOC(R.string.other_document)
}

fun getFoundationTypeItems() = FoundationType.values().map {
  return@map SpinnerItem(it.str, it)
}