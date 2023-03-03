package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.repository.DivisionsRepository
import kotlinx.coroutines.launch

class DivisionsViewModel(application: Application) : BaseViewModel(application) {
  private val _organizationId = MutableLiveData<String?>(null)
  val organizationId: LiveData<String?> get() = _organizationId

  fun setOrganization(organization: Organization) {
    _organizationId.value = organization.id
    loadDivisions(organization.divisionsId)
  }

  private val _currentLevel = MutableLiveData<Int>(0)
  val currentLevel get() = _currentLevel

  private val _divisions = MutableLiveData<List<Division>>(listOf())
  val division get() = _divisions

  private fun loadDivisions(divisionsId: List<String>) {
    val work = Work.LOAD_DIVISIONS
    addWork(work)

    viewModelScope.launch {
      _error.value = DivisionsRepository.getDivisions(divisionsId) { divisions ->
        setDivisions(divisions)
      }
      removeWork(work)
    }
  }

  private fun setDivisions(divisions: List<Division>) {
    _divisions.value = divisions
  }

  var addedDivision: Division? = null

  fun addDivision(division: Division) {
    if (currentLevel.value == 0)
      addDivisionToOrganization(division)
    else addDivisionToDivision(division)

  }

  private fun addDivisionToDivision(division: Division) {
    val organizationId = _organizationId.value ?: return
    division.organization = organizationId
//    division.divisionParentId = currentDivision.value.id


//    _externalAction.value = ADDED_NEW_DIVISION_TO_DIVISION
  }

  private fun addDivisionToOrganization(division: Division) {
    val organizationId = _organizationId.value ?: return
    val work = Work.ADD_DIVISION
    addWork(work)

    division.organization = organizationId

    viewModelScope.launch {
      _error.value =
        DivisionsRepository.addDivisionToOrganization(division, organizationId) { newDivision ->
          addNewDivision(newDivision)
        }
      removeWork(work)
    }
  }

  private fun addNewDivision(newDivision: Division) {
    addedDivision = newDivision
    _externalAction.value =
      if (newDivision.level == 1) Action.ADDED_NEW_DIVISION_TO_ORGANIZATION else Action.ADDED_NEW_DIVISION_TO_DIVISION
    addLocalDivision(newDivision)
  }

  private fun addLocalDivision(newDivision: Division) {
    val divisions = _divisions.value!!.toMutableList()
    divisions.add(newDivision)
    _divisions.value = divisions
  }

  fun afterNotificationAboutAddedDivision() {
    addedDivision = null
    _externalAction.value = null
  }
}