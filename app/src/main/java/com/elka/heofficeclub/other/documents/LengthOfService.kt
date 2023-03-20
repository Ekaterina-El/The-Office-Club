package com.elka.heofficeclub.other.documents

import com.elka.heofficeclub.service.model.documents.forms.Service

data class LengthOfService(
  val total: Service? = null,
  var continuous: Service? = null,
  var toBonus: Service? = null
)