package com.elka.heofficeclub.service.model

import com.elka.heofficeclub.other.Address

data class Organization(
    var id: String = "",
    var fullName: String = "",
    var shortName: String = "",

    var address: Address? = null,

    var organizationHeadId: String = "",
    var organizationHeadLocal: User? = null,

    var humanResourcesDepartmentHeadId: String = "",
    var organizationHumanResourcesDepartmentHeadLocal: User? = null,

    val editors: List<String> = listOf(),
    val divisionsId: List<String> = listOf()
)