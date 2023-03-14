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

class CreateOrganizationViewModel(application: Application) : BaseViewModelWithFields(application) {
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



  override val fields by lazy {
    hashMapOf<Field, MutableLiveData<Any?>>(
      Pair(Field.FULL_NAME, fullName as MutableLiveData<Any?>),
      Pair(Field.SHORT_NAME, shortName as MutableLiveData<Any?>),
      Pair(Field.CITY, city as MutableLiveData<Any?>),
      Pair(Field.STREET, street as MutableLiveData<Any?>),
      Pair(Field.HOUSE, house as MutableLiveData<Any?>),
      Pair(Field.BUILDING, building as MutableLiveData<Any?>),
      Pair(Field.POSTCODE, postcode as MutableLiveData<Any?>),
      Pair(Field.NAME_OF_ORGANIZATION_HEAD, nameOfOrganizationHead as MutableLiveData<Any?>),
      Pair(Field.NAME_OF_HUMAN_RESOURCES_DEPARTMENT_HEAD, nameOfHumanResourcesDepartmentHead as MutableLiveData<Any?>),
      Pair(Field.EMAIL, organizationHeadEmail as MutableLiveData<Any?>),
      Pair(Field.PASSWORD, organizationHeadPassword as MutableLiveData<Any?>),
      Pair(Field.EMAIL_HRD, humanResourcesDepartmentHeadEmail as MutableLiveData<Any?>),
      Pair(Field.PASSWORD_HRD, humanResourcesDepartmentHeadPassword as MutableLiveData<Any?>),
    )
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