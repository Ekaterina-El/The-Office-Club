package com.elka.heofficeclub.other

import com.elka.heofficeclub.R

object Constants {
  const val SP_NAME = "the_office_club"
  const val SEPARATOR = ":"
  const val CREDENTIALS = "credentials"
  const val LOAD_DELAY = 3000L
  const val RESERVED_TO_ADD = "RESERVED_TO_ADD"

  val canChangeHeader = listOf(Role.ORGANIZATION_HEAD)
  val rolesChangeEditor = listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)
  val rolesChangePositions = listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)
  val rolesChangeAboutOrganization =
    listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)

  val months = listOf(
    R.string.jun,
    R.string.feb,
    R.string.march,
    R.string.apr,
    R.string.may,
    R.string.june,
    R.string.jule,
    R.string.aug,
    R.string.sep,
    R.string.oct,
    R.string.nov,
    R.string.dec,
  )
}