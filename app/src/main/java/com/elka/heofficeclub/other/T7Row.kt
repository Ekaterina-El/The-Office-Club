package com.elka.heofficeclub.other

import com.elka.heofficeclub.service.model.Employer
import java.util.Calendar

data class T7Row(
  val division: String = "",
  val position: String = "",
  val fullName: String = "",
  val tableNumber: String = "",
  val vacations: List<T7RowVacation> = listOf(),
): java.io.Serializable {
  override fun toString(): String {
    val vacations = this.vacations.joinToString("; ") { it.toString() }
    return "$division | $position | $fullName [$tableNumber] | $vacations"
  }
}

data class T7RowVacation(
  val countOfDays: String = "", val dateOfStart: String = ""
): java.io.Serializable {
  override fun toString() = "$countOfDays / $dateOfStart"
}

fun List<Employer>.to7Row(): List<T7Row> {
  val rows = mutableListOf<T7Row>()
  val currentYear = getYearOfDate(Calendar.getInstance().time)

  for (employer in this) {
    if (employer.T8 != null) continue

    val t2 = employer.T2Local ?: continue
    val vacations = t2.vocations

    // crate list of vacations date
    val listOfEmployerVacations = mutableListOf<T7RowVacation>()
    for (vacation in vacations) {
      val year = getYearOfDate(vacation.vacationStart)
      if (year != currentYear) continue

      val vacationRow = T7RowVacation(
        countOfDays = vacation.countOfDays.toString(), dateOfStart = vacation.vacationStartS
      )
      listOfEmployerVacations.add(vacationRow)
    }

    // create T7Row
    val row = T7Row(
      division = employer.divisionLocal?.name ?: "",
      position = employer.positionLocal?.name ?: "",
      fullName = t2.fullName,
      tableNumber = t2.tableNumberS,
      vacations = listOfEmployerVacations
    )

    rows.add(row)
  }

  return rows
}