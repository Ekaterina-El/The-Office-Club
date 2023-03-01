package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.FieldErrorType
import com.elka.heofficeclub.other.Validator

abstract class BaseViewModelWithFields(application: Application) : BaseViewModel(application) {
  val requireFields by lazy {
    listOf(
      Field.FULL_NAME, Field.SHORT_NAME, Field.CITY, Field.STREET, Field.HOUSE,
      Field.POSTCODE, Field.NAME_OF_ORGANIZATION_HEAD,
      Field.NAME_OF_HUMAN_RESOURCES_DEPARTMENT_HEAD, Field.EMAIL, Field.PASSWORD,
      Field.EMAIL_HRD, Field.PASSWORD_HRD

    )
  }

  val emailFields by lazy { listOf(Field.EMAIL, Field.EMAIL_HRD) }
  val passwordFields by lazy { listOf(Field.PASSWORD, Field.PASSWORD_HRD) }

  abstract val fields: HashMap<Field, MutableLiveData<String>>

  protected val _fieldErrors = MutableLiveData<List<FieldError>>()
  val fieldErrors get() = _fieldErrors

  fun checkFields(): Boolean {
    val errors = arrayListOf<FieldError>()

    for (field in fields) {
      val willCheckToEmpty = requireFields.contains(field.key)

      if (willCheckToEmpty) {
        val isEmpty = field.value.value?.isEmpty() ?: true
        if (isEmpty) {
          errors.add(FieldError(field.key, FieldErrorType.IS_REQUIRE))
          continue
        }
      }

      val willCheckAsEmail = emailFields.contains(field.key)
      if (willCheckAsEmail) {
        val emailError = Validator.checkEmailField(field.value.value!!)
        if (emailError != null) {
          errors.add(FieldError(field.key, emailError))
          continue
        }
      }

      val willCheckAsPassword = passwordFields.contains(field.key)
      if (willCheckAsPassword) {
        val passwordError = Validator.checkPasswordField(field.value.value!!)
        if (passwordError != null) {
          errors.add(FieldError(field.key, passwordError))
          continue
        }
      }
    }

    _fieldErrors.value = errors
    return _fieldErrors.value!!.isEmpty()
  }
}