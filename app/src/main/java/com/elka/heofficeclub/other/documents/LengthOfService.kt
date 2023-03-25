package com.elka.heofficeclub.other.documents


data class LengthOfService(
  val total: Service? = null,
  var continuous: Service? = null,
  var toBonus: Service? = null
): java.io.Serializable