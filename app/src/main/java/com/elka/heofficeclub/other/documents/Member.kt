package com.elka.heofficeclub.other.documents

data class Member(
  val fullName: String = "",
  var yearOfBirthday: String = "",
  var degreeOfKinship: String = "",
): java.io.Serializable