package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.filterBy
import com.elka.heofficeclub.service.repository.EmployeesRepository
import com.elka.heofficeclub.service.repository.OrganizationPositionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrganizationEmployeesViewModel(application: Application) : BaseViewModel(application) {
  private val _orgPositions = MutableLiveData<List<OrganizationPosition>>(listOf())
  val positions get() = _orgPositions

  private val _orgPositionsFiltered = MutableLiveData<List<OrganizationPosition>>(listOf())
  val orgPositionsFiltered get() = _orgPositionsFiltered

  private var _organization: Organization? = null

  fun setOrganization(organization: Organization) {
    _organization = organization
    loadPositions(organization.positions)
  }

  private fun loadPositions(positionsId: List<String>) {
    val work = Work.LOAD_POSITIONS
    addWork(work)

    viewModelScope.launch {
      delay(1000)
      _error.value = OrganizationPositionRepository.loadPositions(positionsId) { positions ->
        _orgPositions.value = positions
        filterPositions()
      }
      removeWork(work)
    }
  }

  // region Search
  val searchPositions = MutableLiveData("")

  fun filterPositions() {
    val positions = _orgPositions.value!!
    val search = searchPositions.value!!

    _orgPositionsFiltered.value = when(search) {
      "" -> positions
      else -> positions.filterBy(search)
    }
  }

  fun clearPositionSearch() {
    searchPositions.value = ""
    filterPositions()
  }

  // endregion

  fun deletePosition(position: OrganizationPosition) {
    val work = Work.REMOVE_POSITION
    addWork(work)

    viewModelScope.launch {
      _error.value = EmployeesRepository.getCountOfEmployerWithPosition(_organization!!.id, position.id) { count ->
        if (count == 0) {
          _error.value = OrganizationPositionRepository.removePosition(_organization!!.id, position.id) {
            _externalAction.value = Action.REMOVED_POSITION
          }
        } else {
          _error.value = Errors.foundEmployeesWithThisPosition
        }

      }
      removeWork(work)
    }
  }
}