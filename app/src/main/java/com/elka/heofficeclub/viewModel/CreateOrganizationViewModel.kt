package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.service.repository.OrganizationRepository
import com.elka.heofficeclub.service.repository.UsersRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

class CreateOrganizationViewModel(application: Application) : BaseViewModel(application) {
  val fullName = MutableLiveData("")
  val shortName = MutableLiveData("")

  val city = MutableLiveData("")
  val street = MutableLiveData("")
  val house = MutableLiveData("")
  val building = MutableLiveData("")
  val postcode = MutableLiveData("")

  val nameOfOrganizationHead = MutableLiveData("")
  val nameOfHumanResourcesDepartmentHead = MutableLiveData("")

  val organizationHeadEmail = MutableLiveData("")
  val organizationHeadPassword = MutableLiveData("")

  val humanResourcesDepartmentHeadEmail = MutableLiveData("")
  val humanResourcesDepartmentHeadPassword = MutableLiveData("")

  private val _fieldErrors = MutableLiveData<List<FieldError>>(listOf())
  val fieldErrors: LiveData<List<FieldError>> get() = _fieldErrors

  private fun createOrganization() {
    if (organizationHeadEmail.value == humanResourcesDepartmentHeadEmail.value) {
      _fieldErrors.value = listOf(
        FieldError(Field.EMAIL_HRD, FieldErrorType.EMAILS_OF_HEADS_IS_EQUAL)
      )
      return
    }

    val work = Work.CREATE_ORGANIZATION
    addWork(work)

    viewModelScope.launch {
      // check organizationHeadEmail
      val uniqOrganizationHeadEmail = UsersRepository.isUniqEmail(organizationHeadEmail.value!!)
      if (!uniqOrganizationHeadEmail) {
        _error.value = Errors.noUniqOrganizationHeadEmail
        removeWork(work)
        return@launch
      }

      // check humanResourcesDepartmentHeadEmail
      val uniqHumanResourcesDepartmentHeadEmail =
        UsersRepository.isUniqEmail(humanResourcesDepartmentHeadEmail.value!!)
      if (!uniqHumanResourcesDepartmentHeadEmail) {
        _error.value = Errors.noUniqHRDHEmail
        removeWork(work)
        return@launch
      }

      // registration organizationHead
      _error.value = UsersRepository.registrationUser(
        organizationHeadEmail.value!!, organizationHeadPassword.value!!
      ) { organizationHeadId ->

        // registration humanResourcesDepartmentHead
        _error.value = UsersRepository.registrationUser(
          humanResourcesDepartmentHeadEmail.value!!, organizationHeadPassword.value!!
        ) { mHRDH_ID ->

          // add id of organizationHead and humanResourcesDepartmentHead to organization
          val organization = newOrganization
          organization.organizationHeadId = organizationHeadId
          organization.humanResourcesDepartmentHeadId = mHRDH_ID

          // add organization note to org...Collection
          _error.value =
            OrganizationRepository.addOrganization(organization) { newOrganization ->

              // add organizationHead note to usersCollection
              val organizationHeadUser = com.elka.heofficeclub.service.model.User(
                id = organizationHeadId,
                fullName = nameOfOrganizationHead.value!!,
                role = Role.ORGANIZATION_HEAD,
                organizationId = newOrganization.id,
                email = organizationHeadEmail.value!!
              )
              UsersRepository.addUser(organizationHeadUser)


              // add humanResourcesDepartmentHead note to usersCollection
              val humanResourcesDepartmentHeadUser = com.elka.heofficeclub.service.model.User(
                id = mHRDH_ID,
                fullName = nameOfHumanResourcesDepartmentHead.value!!,
                role = Role.HUMAN_RESOURCES_DEPARTMENT_HEAD,
                organizationId = newOrganization.id,
                email = humanResourcesDepartmentHeadEmail.value!!
              )
              UsersRepository.addUser(humanResourcesDepartmentHeadUser)

              _externalAction.value = Action.GO_NEXT
            }
        }
      }

      UsersRepository.logout {}
      removeWork(work)
    }
  }

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

  val fields by lazy {
    hashMapOf<Field, MutableLiveData<String>>(
      Pair(Field.FULL_NAME, fullName),
      Pair(Field.SHORT_NAME, shortName),
      Pair(Field.CITY, city),
      Pair(Field.STREET, street),
      Pair(Field.HOUSE, house),
      Pair(Field.BUILDING, building),
      Pair(Field.POSTCODE, postcode),
      Pair(Field.NAME_OF_ORGANIZATION_HEAD, nameOfOrganizationHead),
      Pair(Field.NAME_OF_HUMAN_RESOURCES_DEPARTMENT_HEAD, nameOfHumanResourcesDepartmentHead),
      Pair(Field.EMAIL, organizationHeadEmail),
      Pair(Field.PASSWORD, organizationHeadPassword),
      Pair(Field.EMAIL_HRD, humanResourcesDepartmentHeadEmail),
      Pair(Field.PASSWORD_HRD, humanResourcesDepartmentHeadPassword),
    )
  }

  private fun checkFields(): Boolean {
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

  private val newOrganization
    get() = com.elka.heofficeclub.service.model.Organization(
      fullName = fullName.value!!,
      shortName = shortName.value!!,
      address = Address(
        city = city.value!!,
        street = street.value!!,
        house = house.value!!,
        building = building.value!!,
        postcode = postcode.value!!
      )
    )

  fun tryCreateOrganization() {
    if (checkFields()) createOrganization()
  }

  fun clearFields() {
    fullName.value = ""
    shortName.value = ""
    city.value = ""
    street.value = ""
    house.value = ""
    building.value = ""
    postcode.value = ""
    nameOfOrganizationHead.value = ""
    nameOfHumanResourcesDepartmentHead.value = ""
    organizationHeadEmail.value = ""
    organizationHeadPassword.value = ""
    _error.value = null
    _fieldErrors.value = listOf()
    _externalAction.value = null
    clearWork()
  }
}