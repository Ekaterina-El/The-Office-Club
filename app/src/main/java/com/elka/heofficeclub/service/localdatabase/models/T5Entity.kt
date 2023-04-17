package com.elka.heofficeclub.service.localdatabase.models

import android.icu.text.Transliterator.Position
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.documents.toTypeOfChangeWork
import com.elka.heofficeclub.other.fromDocFormatToDate
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T5
import java.util.*


@Entity(tableName = "t5")
data class T5Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,
  @ColumnInfo(name = "employer_name") val employerName: String,

  @ColumnInfo(name = "reason") val reason: String,
  @ColumnInfo(name = "type") val type: String,

  @ColumnInfo(name = "old_division") val oldDivision: String,
  @ColumnInfo(name = "new_division") val newDivision: String,

  @ColumnInfo(name = "old_position") val oldPosition: String,
  @ColumnInfo(name = "new_position") val newPosition: String,

  @ColumnInfo(name = "premium") val premuim: Double,
  @ColumnInfo(name = "foundation") val foundation: String,

  @ColumnInfo(name = "start_date") val startDate: String,
  @ColumnInfo(name = "end_date") val endDate: String,
) {
  fun toDocForm() = T5(
    number = number,
    fileUrl = fileUrl,
    dataCreated = dataCreated.fromDocFormatToDate() ?: Date(),
    transferReason = reason,
    employer = Employer(T2Local = T2(firstName = employerName)),
    typeOfChangeWork = type.toTypeOfChangeWork(),
    oldDivision = Division(name = oldDivision),
    oldPosition = OrganizationPosition(name = oldPosition),
    newDivision = Division(name = newDivision),
    newPosition = OrganizationPosition(name = newPosition),
    premium = premuim,
    foundation = foundation,
    transferStart = startDate.fromDocFormatToDate(),
    transferEnd = endDate.fromDocFormatToDate()
  )
}