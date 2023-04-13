package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.to3Row
import com.elka.heofficeclub.other.to7Row
import com.elka.heofficeclub.service.model.*
import com.elka.heofficeclub.service.model.documents.forms.T3
import com.elka.heofficeclub.service.model.documents.forms.T7
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.EmployeesRepository
import com.elka.heofficeclub.service.repository.OrganizationPositionRepository
import com.elka.heofficeclub.service.repository.OrganizationRepository
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
      _error.value = EmployeesRepository.loadEmployers(employeesIdx) { employees ->
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

    _employeesFiltered.value = when (search) {
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

  private var _number = 0
  val number get() = _number

  val createT7Work = Work.CREATE_T7
  fun createT7() {
    if (employees.value!!.isEmpty()) return

    addWork(createT7Work)
    viewModelScope.launch {
      OrganizationRepository.regNextOrderNumber(_organization!!.id) { t7OrderNumber ->
        this@OrganizationEmployeesViewModel._number = t7OrderNumber
        _externalAction.value = Action.GENERATE_T7
      }
    }
  }

  fun getT7() = T7(
    number = number,
    organization = _organization,
    rows = employees.value!!.to7Row()
  )

  fun saveT7(t7: T7, uri: Uri) {
    viewModelScope.launch {
      // load file to server
      _error.value = DocumentsRepository.setFile(_organization!!.id, uri) { fileUri ->

        // set file uri to t7
        t7.fileUrl = fileUri.toString()

        // save t7 to server
        _error.value = DocumentsRepository.setT7(t7) {
          this@OrganizationEmployeesViewModel._number = 0
          removeWork(createT7Work)
          _externalAction.value = Action.AFTER_CREATE_T7
        }
      }

      if (_error.value != null) {
        removeWork(createT7Work)
      }
    }
  }

  val createT3Work = Work.CREATE_T3
  fun createT3() {
    if (employees.value!!.isEmpty()) return

    addWork(createT3Work)
    viewModelScope.launch {
      _error.value = OrganizationRepository.regNextOrderNumber(_organization!!.id) { t3OrderNumber ->
        this@OrganizationEmployeesViewModel._number = t3OrderNumber
        _externalAction.value = Action.GENERATE_T3
      }
    }
  }

  fun getT3(): T3 {
    val rows = employees.value!!.to3Row()
    return T3(
      number = number,
      organization = _organization,
      rows = rows
    )
  }

  fun saveT3(t3: T3, uri: Uri) {
    viewModelScope.launch {
      // load file to server
      _error.value = DocumentsRepository.setFile(_organization!!.id, uri) { fileUri ->

        // set file uri to t3
        t3.fileUrl = fileUri.toString()

        // save t3 to server
        _error.value = DocumentsRepository.setT3(t3) {
          this@OrganizationEmployeesViewModel._number = 0
          removeWork(createT3Work)
          _externalAction.value = Action.AFTER_CREATE_T3
        }
      }

      if (_error.value != null) {
        removeWork(createT3Work)
      }
    }
  }
}