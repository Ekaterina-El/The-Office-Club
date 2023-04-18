package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.fromDocFormatToDate
import com.elka.heofficeclub.other.fromDocFormatWithTime
import com.elka.heofficeclub.service.model.documents.forms.DocForm
import com.elka.heofficeclub.service.model.documents.forms.T3
import com.elka.heofficeclub.service.model.documents.forms.T7
import java.util.*


@Entity(tableName = "t7")
data class T7Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "id") val id: String,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,

  @ColumnInfo(name = "rows") val rows: String,
) {


  companion object {
    const val SEPARATOR = ";"
  }

  fun toDocForm(t7Rows: List<T7RowEntity>, t7VacationRows: List<T7VacationsRowEntity>): DocForm {
    val rowsS = rows.split(SEPARATOR).mapNotNull { id ->
      if (id == "") return@mapNotNull null
      val row = t7Rows.firstOrNull() { it.id == id.toLong() }
      if (row != null) {
        return@mapNotNull row.toT7Row(t7VacationRows)
      } else return@mapNotNull null
    }

    return T7(
      id = id,
      number = number, fileUrl = fileUrl, dataCreated = dataCreated.fromDocFormatWithTime() ?: Date(),
      rows = rowsS
    )
  }
}