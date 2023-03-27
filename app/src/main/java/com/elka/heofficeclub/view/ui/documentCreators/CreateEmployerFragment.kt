package com.elka.heofficeclub.view.ui.documentCreators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.CreateEmployerBinding
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer
import kotlin.collections.HashMap

class CreateEmployerFragment : BaseFragmentEmployer() {
  override val isCreation = true
  private lateinit var binding: CreateEmployerBinding
  private val createEmployerViewModel by activityViewModels<CreateEmployerViewModel>()

  override var positionSpinner: Spinner? = null
  override lateinit var genderSpinner: Spinner
  override lateinit var educationTypeSpinner: Spinner
  override lateinit var postgEducationTypeSpinner: Spinner
  override lateinit var merriedStatusSpinner: Spinner
  override var divisionsSpinner: Spinner? = null
  override lateinit var recyclerViewMembers: RecyclerView

  override val fields: HashMap<Field, Any> by lazy {
    hashMapOf(
      Pair(Field.INN, binding.layoutINN),
      Pair(Field.SNILS, binding.layoutSNILS),
      Pair(Field.EMPLOYER_FULL_NAME, binding.layoutFullName),
      Pair(Field.EMPLOYER_BIRTHDATE, binding.birdthdateError),
      Pair(Field.EMPLOYER_BIRTHPLACE, binding.layoutBirthplace),
      Pair(Field.EMPLOYER_BIRTHPLACE_CODE, binding.layoutBirthplaceCode),
      Pair(Field.EMPLOYER_PHONE_NUMBER, binding.layoutPhoneNumber),
      Pair(Field.EMPLOYER_ADDR_BY_PASS, binding.layoutAddressByPassport),
      Pair(Field.EMPLOYER_ADDR_BY_PASS_POSTCODE, binding.layoutAddressByPassportPostCode),
      Pair(Field.EMPLOYER_ADDR_BY_FACT, binding.layoutAddressInFact),
      Pair(Field.EMPLOYER_ADDR_BY_FACT_POSTCODE, binding.layoutAddressInFactPostcode),
      Pair(Field.EMPLOYER_DATE_OF_REG_ADDR, binding.dateOfRegAddrError),
      Pair(Field.EMPLOYER_PASS_NUMBER, binding.passportNumber),
      Pair(Field.EMPLOYER_PASS_SERIAL, binding.passportSerial),
      Pair(Field.EMPLOYER_PASS_DATE, binding.datePassGivenError),
      Pair(Field.EMPLOYER_PASS_ORG, binding.passportOrganization),
      Pair(Field.CONTRACT_DATE, binding.contractDateError),
      Pair(Field.CONTRACT_NUMBER, binding.layoutContractNumber),
    )
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = createEmployerViewModel

    binding = CreateEmployerBinding.inflate(layoutInflater, container, false)
    binding.apply {
      membersAdapter = this@CreateEmployerFragment.memberAdapter
      lifecycleOwner = viewLifecycleOwner
      master = this@CreateEmployerFragment
      viewModel = this@CreateEmployerFragment.createEmployerViewModel
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    positionSpinner = binding.positionSpinner
    genderSpinner = binding.genderSpinner
    educationTypeSpinner = binding.educationTypeSpinner
    postgEducationTypeSpinner = binding.postgEducationTypeSpinner
    merriedStatusSpinner = binding.merriedStatusSpinner
    divisionsSpinner = binding.divisionsSpinner
    recyclerViewMembers = binding.recyclerViewMembers

    super.onViewCreated(view, savedInstanceState)
  }
}
