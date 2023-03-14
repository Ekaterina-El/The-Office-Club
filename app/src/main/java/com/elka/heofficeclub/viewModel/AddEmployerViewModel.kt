package com.elka.heofficeclub.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class AddEmployerViewModel(application: Application) : BaseViewModelWithFields(application) {
  val fullName = MutableLiveData("")
  val premium = MutableLiveData("")
  val trialPeriod = MutableLiveData("")
  val contractNumber = MutableLiveData("")

  private val _contractDate = MutableLiveData<Date?>(null)
  val contractDate get() = _contractDate

  private val _startWordDate = MutableLiveData<Date?>(null)
  val startWorkDate get() = _startWordDate

  private val _endWordDate = MutableLiveData<Date?>(null)
  val endWorkDate get() = _endWordDate

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
    val premValue = if (premium.value!! == "") 0.0 else premium.value!!.toDouble()
    val premium = (premValue * 100).roundToInt().toDouble() / 100

    val trialPeriodValue = trialPeriod.value
    val trialPeriod = if (trialPeriodValue == "") 0 else trialPeriodValue!!.toInt()

    return T1(
      position = _selectedPositions,
      division = _selectedDivision,

      fullName = fullName.value!!,
      premium = premium,
      trialPeriod = trialPeriod,
      contractNumber = contractNumber.value!!,
    )
  }

  override val fields: HashMap<Field, MutableLiveData<Any?>> = hashMapOf(
    Pair(Field.NAME, fullName as MutableLiveData<Any?>),
    Pair(Field.CONTRACT_NUMBER, contractNumber as MutableLiveData<Any?>),
    Pair(Field.CONTRACT_DATE, _contractDate as MutableLiveData<Any?>),
    Pair(Field.START_WORK_DATE, _startWordDate as MutableLiveData<Any?>),
  )

  fun trySave() {
    if (checkFields()) save()
  }


  private fun save() {
    val d = newDoc
    Log.d(
      "SaveDocument", "pos: ${d.position?.name}, division: ${d.division?.name}, " +
          "fullName: ${d.fullName}, premium: ${d.premium}, contractNumber: ${d.contractNumber}, " +
          "trialPeriod: ${d.trialPeriod}"
    )
  }
}