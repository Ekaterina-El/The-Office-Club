package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.fromDocFormatToDate
import com.elka.heofficeclub.other.fromDocFormatWithTime
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.documents.forms.T11
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T8
import java.util.*

@Entity(tableName = "t11")
data class T11Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "id") val id: String,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,
  @ColumnInfo(name = "employer_name") val employerName: String,

  @ColumnInfo(name = "reason") val reason: String,
  @ColumnInfo(name = "type") val type: String,
  @ColumnInfo(name = "foundation") val foundation: String,
  @ColumnInfo(name = "sum") val sum: Double,
) {
  fun toDocForm() = T11(
    id = id,
    number = number,
    employer = Employer(T2Local = T2(firstName = employerName)),
    fileUrl = fileUrl,
    dataCreated = dataCreated.fromDocFormatWithTime() ?: Date(),
    reason = reason,
    giftType = type,
    sum = sum,
    description = foundation
  )
}