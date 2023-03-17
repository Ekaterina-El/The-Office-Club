package com.elka.heofficeclub.other

import com.elka.heofficeclub.R

object Constants {
  const val SP_NAME = "the_office_club"
  const val SEPARATOR = ":"
  const val CREDENTIALS = "credentials"
  const val LOAD_DELAY = 3000L

  val canChangeHeader = listOf(Role.ORGANIZATION_HEAD)
  val rolesChangeEditor = listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)
  val rolesChangePositions = listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)
  val rolesChangeAboutOrganization =
    listOf(Role.ORGANIZATION_HEAD, Role.HUMAN_RESOURCES_DEPARTMENT_HEAD)

  val months = listOf(
    R.string.jun2,
    R.string.feb2,
    R.string.march2,
    R.string.apr2,
    R.string.may2,
    R.string.june2,
    R.string.jule2,
    R.string.aug2,
    R.string.sep2,
    R.string.oct2,
    R.string.nov2,
    R.string.dec2,
  )
}