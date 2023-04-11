package com.elka.heofficeclub.other

data class Address(
    var city: String = "",
    var street: String = "",
    var house: String = "",
    var building: String = "",
    var postcode: String = ""
): java.io.Serializable
