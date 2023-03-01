package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.Address

data class Organization(
    val id: String = "",
    var fullName: String = "",
    var shortName: String = "",

    var address: Address,

    val nameOfOrganizationHead: String = "",
    val nameOfHumanResourcesDepartmentHead: String = "",
)