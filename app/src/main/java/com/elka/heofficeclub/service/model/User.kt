package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.UserStatus

data class User(
  var id: String = "",
  val fullName: String = "",
  val organizationId: String = "",
  val role: Role = Role.EDITOR,
  val email: String = "",
  var status: UserStatus = UserStatus.UNBLOCKED
)



fun List<User>.filterBy(search: String) = this.filter {
  it.fullName.contains(search, ignoreCase = true)
      || it.email .contains(search, ignoreCase = true)
}
