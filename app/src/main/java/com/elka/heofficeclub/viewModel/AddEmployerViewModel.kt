package com.elka.heofficeclub.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import kotlin.math.roundToInt

class AddEmployerViewModel(application: Application) : BaseViewModelWithFields(application) {
  override val fields: HashMap<Field, MutableLiveData<String>> = hashMapOf()

  val fullName = MutableLiveData("")
  val premium = MutableLiveData("")
  val trialPeriod = MutableLiveData("")
  val contractNumber = MutableLiveData("")

  private val _divisions = MutableLiveData<List<Division>>()
  val divisions get() = _divisions
  fun setDivisions(divisions: List<Division>) {
    _divisions.value = divisions
  }

  private val _positions = MutableLiveData<List<OrganizationPosition>>()
  val positions get() = _positions
  fun setPositions(positions: List<OrganizationPosition>) {
    _positions.value = positions
  }

  private var _selectedDivision: Division? = null
  fun selectDivision(division: Division) {
    _selectedDivision = division
  }

  private var _selectedPositions: OrganizationPosition? = null
  fun selectPosition(organizationPosition: OrganizationPosition) {
    _selectedPositions = organizationPosition
  }

  val newDoc: T1 get() {
    val premium = (premium.value!!.toDouble() * 100).roundToInt().toDouble() / 100
    return T1(
      position = _selectedPositions,
      division = _selectedDivision,

      fullName = fullName.value!!,
      premium = premium,
      trialPeriod = trialPeriod.value!!.toInt(),
      contractNumber = contractNumber.value!!,
    )
  }

}