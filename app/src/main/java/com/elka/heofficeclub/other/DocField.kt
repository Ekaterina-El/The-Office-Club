package com.elka.heofficeclub.other

import com.itextpdf.kernel.geom.Rectangle

class DocField(
  val wrapper: Rectangle,
  val type: FieldType,
  val v: String,
  val justification: Int
) {
  companion object {
    const val LEFT = 0
    const val CENTER = 0
    const val RIGHT = 2
  }
}