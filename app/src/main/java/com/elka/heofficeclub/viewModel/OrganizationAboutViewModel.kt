package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.repository.OrganizationRepository
import com.elka.heofficeclub.service.repository.UsersRepository
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.launch

class OrganizationAboutViewModel(application: Application) : BaseViewModelWithFields(application) {
  val fullName = MutableLiveData("")
  val shortName = MutableLiveData("")
  val okpo = MutableLiveData("")

  val city = MutableLiveData("")
  val street = MutableLiveData("")
  val house = MutableLiveData("")
  val building = MutableLiveData("")
  val postcode = MutableLiveData("")

  private val _organization = MutableLiveData<Organization?>(null)
  val organization get() = _organization

  fun setOrganization(organization: Organization) {
    _organization.value = organization
    fullName.value = organization.fullName
    shortName.value = organization.shortName
    city.value = organization.address.city
    street.value = organization.address.street
    house.value = organization.address.house
    building.value = organization.address.building
    postcode.value = organization.address.postcode
    okpo.value = organization.okpo
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
      Pair(Field.OKPO, okpo as MutableLiveData<Any?>),
    )
  }

  private val newOrganization
    get() = _organization.value?.copy(
      fullName = fullName.value!!,
      shortName = shortName.value!!,
      okpo = okpo.value!!,
      address = Address(
        city = city.value!!,
        street = street.value!!,
        house = house.value!!,
        building = building.value!!,
        postcode = postcode.value!!
      )
    )


  private fun saveChanges() {
    val id = organization.value?.id ?: return
    val organization = newOrganization ?: return

    val work = Work.SAVE_CHANGES
    addWork(work)

    viewModelScope.launch {
      _error.value = OrganizationRepository.updateOrganization(id, organization) { newOrganization ->
        _organization.value = newOrganization
        _externalAction.value = Action.ORGANIZATION_UPDATED
      }
      removeWork(work)
    }
  }

  fun trySaveChanges() {
    if (checkFields()) saveChanges()
  }
}