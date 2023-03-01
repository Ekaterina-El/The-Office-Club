package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.Address

data class Organization(
    var id: String = "",
    var fullName: String = "",
    var shortName: String = "",

    var address: Address,

    val nameOfOrganizationHead: String = "",
    val nameOfHumanResourcesDepartmentHead: String = "",

    var organizationHeadId: String = "",
    var humanResourcesDepartmentHeadId: String = "",
    val editors: List<String> = listOf()
)