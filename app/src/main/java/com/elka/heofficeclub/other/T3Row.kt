package com.elka.heofficeclub.other

import com.elka.heofficeclub.service.model.Employer

data class T3Row(
  val division: String = "",
  val position: String = "",
  val countOfEmployees: String = "",
  val salary: String = "",
  val premium: String = "",
  val info: String = "",
) {
  var totalMonth: String get() = (salary.toDouble() * countOfEmployees.toInt() + premium.toDouble()).toString()
  set(v) {}

  override fun toString(): String {
    return "$division | $position | $countOfEmployees | $salary | $premium | $totalMonth | $info"
  }
}

fun List<Employer>.to3Row(): List<T3Row> {
  val rows = mutableListOf<T3Row>()

  val divisions = this.mapNotNull { it.divisionLocal }.toSet()

  for (division in divisions) {
    val positionsInDivision =
      this.mapNotNull { if (it.divisionId == division.id) it.positionLocal else null }.toSet()

    val divisionRows = positionsInDivision.mapNotNull { it ->
      val employeesDivisionPosition =
        this.filter { employer -> employer.positionId == it.id && employer.divisionId == division.id && employer.T8 == null}
      val countOfEmployers = employeesDivisionPosition.size
      if (countOfEmployers == 0) return@mapNotNull null

      val premium = employeesDivisionPosition.sumOf { it.premium }.toString()

      return@mapNotNull T3Row(
        division = division.name,
        position = it.name,
        countOfEmployees = countOfEmployers.toString(),
        salary = it.salary.toString(),
        premium = premium,
        info = ""
      )
    }

    rows.addAll(divisionRows)
  }

  return rows
}