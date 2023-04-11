package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.*
import com.elka.heofficeclub.service.repository.EmployeesRepository
import com.elka.heofficeclub.service.repository.OrganizationPositionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrganizationEmployeesViewModel(application: Application) : BaseViewModel(application) {
  private var _organization: Organization? = null

  fun setOrganization(organization: Organization) {
    _organization = organization
    loadPositions(organization.positions)
    loadEmployees(organization.employees)
  }

  // region Employees
  private val _employees = MutableLiveData<List<Employer>>(listOf())
  val employees get() = _employees

  private val _employeesFiltered = MutableLiveData<List<Employer>>(listOf())
  val employeesFiltered get() = _employeesFiltered

  private fun loadEmployees(employeesIdx: List<String>) {
    val work = Work.LOAD_EMPLOYERS
    addWork(work)

    viewModelScope.launch {
      _error.value  = EmployeesRepository.loadEmployers(employeesIdx) { employees ->
        _employees.value = employees.splitByT8()
        filterEmployees()
        removeWork(work)
      }

      if (_error.value != null) removeWork(work)
    }
  }

  // region Search employer
  val searchEmployees = MutableLiveData("")

  fun filterEmployees() {
    val items = _employees.value!!
    val search = searchEmployees.value!!

    _employeesFiltered.value = when(search) {
      "" -> items
      else -> items.filterBy(search)
    }
  }
  // endregion

  // region Positions
  private val _orgPositions = MutableLiveData<List<OrganizationPosition>>(listOf())
  val positions get() = _orgPositions

  private val _orgPositionsFiltered = MutableLiveData<List<OrganizationPosition>>(listOf())
  val orgPositionsFiltered get() = _orgPositionsFiltered

  private fun loadPositions(positionsId: List<String>) {
    val work = Work.LOAD_POSITIONS
    addWork(work)

    viewModelScope.launch {
      _error.value = OrganizationPositionRepository.loadPositions(positionsId) { positions ->
        _orgPositions.value = positions
      }
      removeWork(work)
    }
  }

  fun clearEmployerSearch() {
    searchEmployees.value = ""
    filterEmployees()
  }
  // endregion

}