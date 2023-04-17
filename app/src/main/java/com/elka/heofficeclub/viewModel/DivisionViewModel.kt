package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.repository.DivisionsRepository
import com.elka.heofficeclub.service.repository.EmployeesRepository
import com.elka.heofficeclub.service.repository.OrganizationPositionRepository
import kotlinx.coroutines.launch

class DivisionViewModel(application: Application) : BaseViewModel(application) {
  fun clear() {

  }

  private val _orgPositions = MutableLiveData<List<OrganizationPosition>>(listOf())
  val positions get() = _orgPositions
  private fun loadPositions(orgId: String) {
    val work = Work.LOAD_POSITIONS
    addWork(work)

    viewModelScope.launch {
      _error.value = OrganizationPositionRepository.loadPositionsByOrgId(orgId) { positions ->
        _orgPositions.value = positions
      }
      removeWork(work)
    }
  }

  private val _division = MutableLiveData<Division?>(null)
  val division get() = _division
  fun setDivision(division: Division) {
    _division.value = division
    loadEmployees(division.employees)
    loadPositions(division.organization)
  }


  // region Employees
  private val _employees = MutableLiveData<List<Employer>>(listOf())
  val employees get() = _employees
  fun loadEmployees(ids: List<String>) {
    val work = Work.LOAD_EMPLOYERS
    addWork(work)

    viewModelScope.launch {
      _error.value = EmployeesRepository.loadEmployers(ids) { employees ->
        _employees.value = employees
        filterEmployees()
        removeWork(work)
      }

      if (_error.value != null) removeWork(work)
    }
  }
  // endregion

  // region Search employees
  val searchEmployees = MutableLiveData("")

  private val _employeesFiltered = MutableLiveData<List<Employer>>(listOf())
  val employeesFiltered get() = _employeesFiltered

  fun filterEmployees() {
    val items = _employees.value!!
    val search = searchEmployees.value!!

    _employeesFiltered.value = when (search) {
      "" -> items
      else -> items.filterBy(search)
    }

    Log.d("filterEmployees", "items size: $items")
  }

  fun clearEmployerSearch() {
    searchEmployees.value = ""
    filterEmployees()
  }

  fun reloadCurrentDivision(after: (() -> Unit) = {}) {
    val division = _division.value ?: return

    val work = Work.LOAD_DIVISION
    addWork(work)

    viewModelScope.launch {
      _error.value = DivisionsRepository.loadDivision(division.id) {
        setDivision(it)
        after()
      }
    }
  }
  // endregion


  private val _showBottomMenu = MutableLiveData(true)
  val showBottomMenu get() = _showBottomMenu

  fun setBottomMenuStatus(show: Boolean) {
    _showBottomMenu.value = show
  }
}