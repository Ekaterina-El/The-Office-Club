package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.repository.EmployeesRepository
import kotlinx.coroutines.launch

class DivisionViewModel(application: Application) : BaseViewModel(application) {
  fun clear() {

  }

  private val _division = MutableLiveData<Division?>(null)
  val division get() = _division
  fun setDivision(division: Division) {
    _division.value = division
    loadEmployees(division.employees)
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
  // endregion
}