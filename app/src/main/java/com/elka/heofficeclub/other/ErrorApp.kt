package com.elka.heofficeclub.other

import com.elka.heofficeclub.R

data class ErrorApp(val messageRes: Int)

object Errors {
    val noUniqOrganizationHeadEmail = ErrorApp(R.string.no_uniq_organization_head_email)
    val noUniqHRDHEmail = ErrorApp(R.string.no_uniq_HRDH_email)
    val invalidEmailPassword = ErrorApp(R.string.invalid_email_password)
    val unknown = ErrorApp(R.string.unknown_error)
    val network = ErrorApp(R.string.network_error)
    val weakPassword = ErrorApp(R.string.weak_password)
    val userCollision = ErrorApp(R.string.user_collision)
}