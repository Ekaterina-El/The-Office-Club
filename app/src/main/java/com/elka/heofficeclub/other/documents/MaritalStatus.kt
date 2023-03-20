package com.elka.heofficeclub.other.documents

enum class MaritalStatus(val code: Int) {
  NeverMarried(1),
  IsInARegisteredMarriage(2),
  IsInAUnregisteredMarriage(3),
  WidowerWidow(4),
  OfficiallyDivorced(5),
  Separated(6),
}