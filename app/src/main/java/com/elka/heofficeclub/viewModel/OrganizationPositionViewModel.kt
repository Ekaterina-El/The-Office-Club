package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.service.model.OrganizationPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class OrganizationPositionViewModel(application: Application) :
  BaseViewModelWithFields(application) {
  val name = MutableLiveData("")
  val salary = MutableLiveData("")

  override val fields: HashMap<Field, MutableLiveData<String>> = hashMapOf(
    Pair(Field.NAME, name),
    Pair(Field.SALARY, salary),
  )

  private val newOrganizationPosition: OrganizationPosition
    get() {
      var salary = salary.value?.toDouble() ?: 0.0
      salary = (salary * 100).roundToInt().toDouble() / 100

      return OrganizationPosition(
        name = name.value!!,
        salary = salary
      )
    }

  fun clear() {
    clearWork()
    name.value = ""
    salary.value = ""
    _fieldErrors.value = listOf()
    _addedPosition.value = null
  }

  private val _addedPosition = MutableLiveData<OrganizationPosition?>(null)
  val addedPosition get() = _addedPosition

  fun tryCreatePosition() {
    if (checkFields()) createPosition()
  }

  private fun createPosition() {
    val work = Work.ADD_ORGANIZATION_POSITION
    addWork(work)

    val position = newOrganizationPosition

    viewModelScope.launch {
      delay(2000)
      _addedPosition.value = position
    }
  }
}