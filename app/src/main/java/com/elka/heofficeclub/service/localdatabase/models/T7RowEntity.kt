package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "t7_rows")
data class T7RowEntity(
  @PrimaryKey(autoGenerate = true) var id: Int,
  @ColumnInfo(name = "count_of_days") val countOfDays: String,
  @ColumnInfo(name = "date_start") val dateStart: String,
)