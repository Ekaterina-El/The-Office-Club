package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.Role

data class User(
  var id: String = "",
  val fullName: String = "",
  val organizationId: String = "",
  val role: Role = Role.EDITOR,
  val email: String = ""
)
