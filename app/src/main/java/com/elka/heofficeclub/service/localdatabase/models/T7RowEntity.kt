package com.elka.heofficeclub.service.localdatabase.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elka.heofficeclub.other.T7Row
import com.elka.heofficeclub.other.T7RowVacation


@Entity(tableName = "t7_rows")
data class T7RowEntity(
  @PrimaryKey var id: Long,
  @ColumnInfo(name = "division") val division: String,
  @ColumnInfo(name = "position") val position: String,
  @ColumnInfo(name = "full_name") val fullName: String,
  @ColumnInfo(name = "table_number") val tableNumber: String,
  @ColumnInfo(name = "vacations") val vacations: String,

  ) {
  fun toT7Row(t7VacationRows: List<T7VacationsRowEntity>): T7Row {

    val vacations = vacations.split(T7Entity.SEPARATOR).mapNotNull { id ->
      if (id == "") return@mapNotNull null
      val row = t7VacationRows.firstOrNull() { it.id == id.toLong() }
      if (row != null) {
        return@mapNotNull row.toT7VacationsRow()
      } else return@mapNotNull null
    }

    return T7Row(division, position, fullName, tableNumber, vacations)
  }
}


@Entity(tableName = "t7_vacations_rows")
data class T7VacationsRowEntity(
  @PrimaryKey var id: Long,
  @ColumnInfo(name = "count_of_days") val countOfDays: String,
  @ColumnInfo(name = "date_start") val dateStart: String,
) {
  fun toT7VacationsRow() = T7RowVacation(countOfDays, dateStart)
}