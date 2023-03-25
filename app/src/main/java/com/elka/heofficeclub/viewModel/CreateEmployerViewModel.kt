package com.elka.heofficeclub.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.repository.DocumentsRepository
import com.elka.heofficeclub.service.repository.EmployeesRepository
import com.elka.heofficeclub.service.repository.OrganizationRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

class CreateEmployerViewModel(application: Application) : BaseViewModelEmployer(application) {
  override val screens: Int = 6

  private fun toCreateEmployer() {
    val work = Work.CREATE_EMPLOYER
    addWork(work)

    viewModelScope.launch {
      // get employer table number
      _error.value = OrganizationRepository.regNextTableNumber(organization!!.id) { tableNumber ->

        // create user
        val employer =
          Employer(tableNumber = tableNumber, divisionId = "", organizationId = organization!!.id)
        _error.value = EmployeesRepository.createEmployer(employer) { newEmployer ->

          // register T2 doc number
          _error.value =
            OrganizationRepository.regNextOrderNumber(organization!!.id) { orderNumber ->

              // create T2
              val t2 = newT2
              t2.number = orderNumber
              t2.dataCreated = Calendar.getInstance().time
              t2.orgId = organization!!.id
              t2.orgName = organization!!.fullName
              t2.codeOKPO = organization!!.okpo
              t2.tableNumber = newEmployer.tableNumber
              t2.alphabet = t2.lastName[0].toString()


              _error.value = DocumentsRepository.setT2(t2) { t2End ->

                createdT2 = t2End

                // register T1 doc number
                _error.value =
                  OrganizationRepository.regNextOrderNumber(organization!!.id) { t1OrderNumber ->
                    val premValue = if (premium.value!! == "") 0.0 else premium.value!!.toDouble()
                    val premium = (premValue * 100).roundToInt().toDouble() / 100

                    newT1 = T1(
                      number = t1OrderNumber,
                      orgId = organization!!.id,
                      orgName = organization!!.fullName,
                      codeOKPO = organization!!.okpo,
                      dataCreated = getCurrentTime(),
                      fullName = t2.fullName,
                      employerTableNumber = t2.tableNumber,

                      position = _selectedPosition,
                      division = selectedDivision,
                      conditionOfWork = conditionOfWork.value!!,
                      natureOfWork = natureOfWork.value!!,
                      premium = premium,
                      trialPeriod = trialPeriod.value!!.toIntOrEmpty(),
                      contractData = _contractDate.value,
                      contractNumber = contractNumber.value!!,

                      hiredFrom = _hiredFrom.value,
                      hiredBy = _hiredBy.value,
                    )
                    _externalAction.value = Action.CREATE_FILE_T1
                  }

              }
            }
        }
      }
    }

  }

  override fun onEndScreens() {
    toCreateEmployer()
  }

}

