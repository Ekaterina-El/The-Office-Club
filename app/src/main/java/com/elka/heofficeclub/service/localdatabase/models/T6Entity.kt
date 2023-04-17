package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "t6")
data class T6Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,

  @ColumnInfo(name = "work_start") val workStart: String,
  @ColumnInfo(name = "work_end") val workEnd: String,

  @ColumnInfo(name = "vacation_a_start") val vacationAStart: String,
  @ColumnInfo(name = "vacation_a_end") val vacationAEnd: String,

  @ColumnInfo(name = "vacation_b_start") val vacationBStart: String,
  @ColumnInfo(name = "vacation_b_end") val vacationBEnd: String,
)