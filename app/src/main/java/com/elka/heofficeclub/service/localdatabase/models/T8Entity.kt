package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.fromDocFormatToDate
import com.elka.heofficeclub.other.fromDocFormatWithTime
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T8
import java.util.Date


@Entity(tableName = "t8")
data class T8Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "id") val id: String,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,
  @ColumnInfo(name = "employer_name") val employerName: String,

  @ColumnInfo(name = "date_dismissal") val dateDismissal: String,
  @ColumnInfo(name = "foundation") val foundation: String,
  @ColumnInfo(name = "foundation_doc") val foundationDoc: String,
  @ColumnInfo(name = "foundation_number") val foundationNumber: String,
  @ColumnInfo(name = "foundation_date") val foundationDate: String,
) {
  fun toDocForm() = T8(
    id = id,
    number = number,
    employer = Employer(T2Local = T2(firstName = employerName)),
    fileUrl = fileUrl,
    dataCreated = dataCreated.fromDocFormatWithTime() ?: Date(),
    dismissalDate = dateDismissal.fromDocFormatToDate(),
    reason = foundation,
    reasonDate = foundationDate.fromDocFormatToDate(),
    reasonDoc = foundationDoc,
    reasonNumber = foundationNumber
    )
}