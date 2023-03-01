package com.elka.heofficeclub.other

import com.elka.heofficeclub.R

data class FieldError(val field: Field, var errorType: FieldErrorType?)

enum class FieldErrorType(val messageRes: Int) {
    IS_REQUIRE(R.string.is_require), IS_NOT_EMAIL(R.string.is_no_email), SHORT(R.string.short_error)
}

enum class Field {
    EMAIL, PASSWORD, FULL_NAME, SHORT_NAME, CITY,
    STREET, HOUSE, BUILDING, POSTCODE, NAME_OF_ORGANIZATION_HEAD,
    NAME_OF_HUMAN_RESOURCES_DEPARTMENT_HEAD

}