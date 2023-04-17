package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.fromDocFormatToDate
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T6
import java.util.*


@Entity(tableName = "t6")
data class T6Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,
  @ColumnInfo(name = "employer_name") val employerName: String,

  @ColumnInfo(name = "work_start") val workStart: String,
  @ColumnInfo(name = "work_end") val workEnd: String,

  @ColumnInfo(name = "vacation_a_start") val vacationAStart: String,
  @ColumnInfo(name = "vacation_a_end") val vacationAEnd: String,

  @ColumnInfo(name = "vacation_b_start") val vacationBStart: String,
  @ColumnInfo(name = "vacation_b_end") val vacationBEnd: String,
) {
  fun toDocForm() = T6(
    number = number,
    fileUrl = fileUrl,
    dataCreated = dataCreated.fromDocFormatToDate() ?: Date(),
    employer = Employer( T2Local = T2(firstName = employerName)),
    startWork = workStart.fromDocFormatToDate(),
    endWork = workEnd.fromDocFormatToDate(),
    startVacationA = vacationAStart.fromDocFormatToDate(),
    endVacationA = vacationAEnd.fromDocFormatToDate(),
    startVacationB = vacationBStart.fromDocFormatToDate(),
    endVacationB = vacationBEnd.fromDocFormatToDate(),
  )
}