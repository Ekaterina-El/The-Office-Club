package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.service.model.OrganizationPosition
import kotlin.math.roundToInt

class OrganizationPositionViewModel(application: Application) :
  BaseViewModelWithFields(application) {
  val name = MutableLiveData("")
  val salary = MutableLiveData("0")

  override val fields: HashMap<Field, MutableLiveData<String>> = hashMapOf(
    Pair(Field.NAME, name),
    Pair(Field.SALARY, salary),
  )

  val newOrganizationPosition: OrganizationPosition
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
  }
}