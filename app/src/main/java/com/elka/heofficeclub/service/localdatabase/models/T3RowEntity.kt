package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.T3Row

@Entity(tableName = "t3_rows")
data class T3RowEntity(
  @PrimaryKey var id: Long,
  @ColumnInfo(name = "division") val division: String,
  @ColumnInfo(name = "position") val position: String,
  @ColumnInfo(name = "count_of_employers") val countOfEmployers: String,
  @ColumnInfo(name = "salary") val salary: String,
  @ColumnInfo(name = "premium") val premium: String,
) {
  fun toT3Row() = T3Row(
    division, position, countOfEmployers, salary, premium
  )
}