package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.model.getDivisionById
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
  private val _filteredDivisions = MutableLiveData<List<Division>>(listOf())
  val filteredDivisions get() = _filteredDivisions

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
    filterDivisions()
  }

  val divisionFilter = MutableLiveData("")
  fun filterDivisions() {
    val filter = divisionFilter.value!!
    val divisions = _divisions.value!!

    _filteredDivisions.value = when {
      filter.isEmpty() -> divisions
      else -> divisions.filterBy(filter)
    }
  }

  fun clearDivisionFilter() {
    divisionFilter.value = ""
    filterDivisions()
  }

  private fun changeListOfDivisions(division: Division, action: Action) {
    val divisions = _divisions.value!!.toMutableList()

    when(action) {
      Action.REMOVE -> divisions.remove(division)
      Action.ADD -> divisions.add(division)
      else -> null
    }

    _divisions.value = divisions
    filterDivisions()
  }

  var addedDivision: Division? = null
  fun addDivision(division: Division) {
    val organizationId = _organizationId.value ?: return
    val work = Work.ADD_DIVISION
    addWork(work)

    division.organization = organizationId

    viewModelScope.launch {
      _error.value =
        DivisionsRepository.addDivision(division, organizationId) { newDivision ->
          addNewDivision(newDivision)
        }
      removeWork(work)
    }
  }

  private fun addNewDivision(newDivision: Division) {
    addedDivision = newDivision
    _externalAction.value = Action.ADDED_NEW_DIVISION_TO_ORGANIZATION
    addLocalDivision(newDivision)
  }

  private fun addLocalDivision(newDivision: Division) {
    changeListOfDivisions(newDivision, Action.ADD)
  }

  fun afterNotificationAboutChangedDivisions() {
    removedDivision = null
    addedDivision = null
    _externalAction.value = null
  }

  var removedDivision: Division? = null
  fun deleteDivision(division: Division) {
    val work = Work.REMOVE_DIVISION
    addWork(work)

    viewModelScope.launch {
      _error.value = DivisionsRepository.deleteDivision(division) {
        removedDivision = division
        _externalAction.value = Action.REMOVED_DIVISION
        removeLocalDivision(division)
      }
      removeWork(work)
    }
  }

  private fun removeLocalDivision(removedDivision: Division) {
    changeListOfDivisions(removedDivision, Action.REMOVE)
  }
}

