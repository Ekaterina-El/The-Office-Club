package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "t5")
data class T5Entity(
  @PrimaryKey var number: Int,
  @ColumnInfo(name = "file_url") val fileUrl: String,
  @ColumnInfo(name = "date_created") val dataCreated: String,

  @ColumnInfo(name = "reason") val reason: String,
  @ColumnInfo(name = "type") val type: String,

  @ColumnInfo(name = "old_division") val oldDivision: String,
  @ColumnInfo(name = "new_division") val newDivision: String,

  @ColumnInfo(name = "old_position") val oldPosition: String,
  @ColumnInfo(name = "new_position") val newPosition: String,

  @ColumnInfo(name = "premium") val premuim: Double,
  @ColumnInfo(name = "foundation") val foundation: String,

  @ColumnInfo(name = "start_date") val startDate: String,
  @ColumnInfo(name = "start_date") val endDate: String,
)