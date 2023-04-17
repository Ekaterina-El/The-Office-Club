package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.fromDocFormatToDate
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import java.util.*


@Entity(tableName = "t1")
data class T1Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "id") val id: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,

  @ColumnInfo(name = "hired_from") var hiredFrom: String,
  @ColumnInfo(name = "hired_by") var hiredBy: String,

  @ColumnInfo(name = "division_id") var divisionId: String,
  @ColumnInfo(name = "division_name") var divisionName: String,
  @ColumnInfo(name = "employer_name") var employerName: String,

  @ColumnInfo(name = "position") var position: String,

  @ColumnInfo(name = "premium") var premium: Double = 0.0,
  @ColumnInfo(name = "trial_period") var trialPeriod: Int = 0,

  @ColumnInfo(name = "contract_date") var contractData: String,
  @ColumnInfo(name = "contract_number") var contractNumber: String,

  @ColumnInfo(name = "condition_of_work") var conditionOfWork: String = "",
  @ColumnInfo(name = "nature_of_work") var natureOfWork: String = "",
) {
  fun toDocForm() = T1(
    id = id,
    fileUrl = fileUrl,
    number = number,
    dataCreated = dataCreated.fromDocFormatToDate() ?: Date(),
    hiredBy = hiredBy.fromDocFormatToDate(),
    hiredFrom = hiredFrom.fromDocFormatToDate(),
    division = Division(id = divisionId, name = divisionName),
    position = OrganizationPosition(name = position),
    premium = premium,
    fullName = employerName,
    trialPeriod = trialPeriod,
    contractData = contractData.fromDocFormatToDate(),
    contractNumber = contractNumber,
    conditionOfWork = conditionOfWork,
  )
}