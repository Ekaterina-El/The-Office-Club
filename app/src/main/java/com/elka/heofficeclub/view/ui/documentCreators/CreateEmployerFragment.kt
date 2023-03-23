package com.elka.heofficeclub.view.ui.documentCreators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.CreateEmployerBinding
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.Constants.RESERVED_TO_ADD
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.users.MemberViewHolder
import com.elka.heofficeclub.view.list.users.MembersAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.google.android.material.textfield.TextInputLayout
import kotlin.collections.HashMap

class CreateEmployerFragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: CreateEmployerBinding
  private val viewModel by activityViewModels<CreateEmployerViewModel>()

  private lateinit var memberAdapter: MembersAdapter
  private val memberListener by lazy {
    object : MemberViewHolder.Companion.Listener {

      override fun onDelete(pos: Int) {
        memberAdapter.removeByPos(pos)
      }
    }
  }

  private val organizationObserver = Observer<Organization> {
  }

  private val positionsObserver = Observer<List<OrganizationPosition>> {
    val items = it.toMutableList()
    items.add(OrganizationPosition(id = RESERVED_TO_ADD, name = getString(R.string.add_position)))
    val spinnerAdapter = OrgPositionsSpinnerAdapter(requireContext(), items)
    binding.positionSpinner.adapter = spinnerAdapter
  }

  private val divisionsObserver = Observer<List<Division>> {
    val spinnerAdapter = DivisionsSpinnerAdapter(requireContext(), it)
    binding.divisionsSpinner.adapter = spinnerAdapter
  }

  override val externalActionObserver = Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.CREATE_FILE_T1 -> {
        val t1 = viewModel.newT1 ?: return@Observer
        val uri = DocumentCreator(requireContext()).createFormT1(t1)

        viewModel.saveT1(t1, uri)
      }

      Action.GO_BACK -> {
        memberAdapter.clear()
        goBack()
      }

      else -> Unit
    }
  }


  private val fieldErrorsObserver = Observer<List<FieldError>> {
    showErrors(it)
  }

  private val fields: HashMap<Field, Any> by lazy {
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

  private fun showErrors(errors: List<FieldError>?) {
    for (field in fields) {
      val error = errors?.firstOrNull { it.field == field.key }
      val errorStr = error?.let { getString(it.errorType!!.messageRes) } ?: ""

      when (val f = field.value) {
        is TextInputLayout -> f.error = errorStr
        is TextView -> f.text = errorStr
        else -> Unit
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    memberAdapter = MembersAdapter(memberListener)
    binding = CreateEmployerBinding.inflate(layoutInflater, container, false)
    binding.apply {
      membersAdapter = this@CreateEmployerFragment.memberAdapter
      lifecycleOwner = viewLifecycleOwner
      master = this@CreateEmployerFragment
      viewModel = this@CreateEmployerFragment.viewModel
    }

    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    organizationViewModel.setBottomMenuStatus(false)
    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        viewModel.goPrevScreen()
      }
    })
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initSpinners()

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.recyclerViewMembers.addItemDecoration(decorator)
  }

  private fun initSpinners() {
    // Genders spinner
    val spinnerAdapter = SpinnerAdapter(requireContext(), getGenderSpinnerItems())
    binding.genderSpinner.adapter = spinnerAdapter

    // Education spinner
    val educationsAdapter = SpinnerAdapter(requireContext(), getEducationSpinnerItems())
    binding.educationTypeSpinner.adapter = educationsAdapter

    // Postg education spinner
    val postgEducationsAdapter =
      SpinnerAdapter(requireContext(), getPostgraduateEducationSpinnerItems())
    binding.postgEducationTypeSpinner.adapter = postgEducationsAdapter

    // Married Status spinner
    val marriedStatusAdapter = SpinnerAdapter(requireContext(), getMarriedStatusSpinnerItems())
    binding.merriedStatusSpinner.adapter = marriedStatusAdapter
  }

  fun goNextScreen() {
    if (viewModel.screen.value == 3) viewModel.setFamilyMembers(memberAdapter.getMembers())
    viewModel.goNextScreen()
  }

  private fun goBack() {
    viewModel.clear()
    organizationViewModel.setBottomMenuStatus(true)
    navController.popBackStack()
  }

  private val divisionSpinnerListener by lazy {
    Selector { viewModel.selectDivision(it as Division) }
  }

  private val positionSpinnerListener by lazy {
    Selector {
      val position = it as OrganizationPosition
      if (position.id == RESERVED_TO_ADD) {
        addOrganizationPosition()
      } else viewModel.selectPosition(position)
    }
  }

  private val organizationPositionDialogListener by lazy {
    object : OrganizationPositionDialog.Companion.Listener {
      override fun agree(organizationPosition: OrganizationPosition) {
        organizationViewModel.addPosition(organizationPosition)
        viewModel.addPosition(organizationPosition)
      }
    }
  }

  private val organizationPositionDialog by lazy {
    OrganizationPositionDialog(
      requireContext(),
      viewModelOwner = this,
      viewLifecycleOwner,
      organizationViewModel.organization.value!!.id,
      organizationPositionDialogListener
    )
  }

  private fun addOrganizationPosition() {
    organizationPositionDialog.open()
  }

  override fun onResume() {
    super.onResume()


    viewModel.error.observe(this, errorObserver)
    viewModel.fieldErrors.observe(this, fieldErrorsObserver)
    viewModel.externalAction.observe(this, externalActionObserver)
    viewModel.divisions.observe(this, divisionsObserver)
    viewModel.positions.observe(this, positionsObserver)
    viewModel.work.observe(this, workObserver)


    binding.genderSpinner.onItemSelectedListener = genderSpinnerListener
    binding.educationTypeSpinner.onItemSelectedListener = educationSpinnerListener
    binding.postgEducationTypeSpinner.onItemSelectedListener = postgEducationSpinnerListener
    binding.merriedStatusSpinner.onItemSelectedListener = marriedSpinnerListener
    binding.divisionsSpinner.onItemSelectedListener = divisionSpinnerListener
    binding.positionSpinner.onItemSelectedListener = positionSpinnerListener
  }

  override fun onStop() {
    super.onStop()
    viewModel.error.removeObserver(errorObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)
    viewModel.work.removeObserver(workObserver)

    binding.genderSpinner.onItemSelectedListener = null
    binding.educationTypeSpinner.onItemSelectedListener = null
    binding.postgEducationTypeSpinner.onItemSelectedListener = null
    binding.merriedStatusSpinner.onItemSelectedListener = null
    binding.divisionsSpinner.onItemSelectedListener = null
    binding.positionSpinner.onItemSelectedListener = null
  }

  private val genderSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val gender = spinner.value as Gender
      viewModel.setGender(gender)
    }
  }

  private val educationSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val education = spinner.value as EducationType
      viewModel.setEducationType(education)
    }
  }

  private val postgEducationSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val education = spinner.value as PostgraduateVocationalEducationType
      viewModel.setPostgEducationType(education)
    }
  }

  private val marriedSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val marriedStatus = spinner.value as MaritalStatus
      viewModel.setMarriedStatus(marriedStatus)
    }
  }

  private val datePickerListener = object : Companion.DatePickerListener {
    override fun onPick(date: Date) {
      viewModel.saveDate(date)
    }
  }

  fun showRegAccorinigAddressPicker() = showDatePicker(DateType.REG_ACCORINING_ADDRESS)
  fun showBirthdatePicker() = showDatePicker(DateType.BIRTDAY)
  fun showPassportDatePicker() = showDatePicker(DateType.PASSPORT_DATE)
  fun showContractDatePicker() = showDatePicker(DateType.CONTRACT)
  fun showStartWorkDatePicker() = showDatePicker(DateType.START_WORK)
  fun showEndWorkDatePicker() = showDatePicker(DateType.END_WORK)

  private fun showDatePicker(type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.BIRTDAY -> viewModel.birthdate.value
      DateType.PASSPORT_DATE -> viewModel.passportDate.value
      DateType.CONTRACT -> viewModel.contractDate.value
      DateType.REG_ACCORINING_ADDRESS -> viewModel.dateOfRegAccorinigAddress.value
      DateType.START_WORK -> viewModel.hiredFrom.value
      DateType.END_WORK -> viewModel.hiredBy.value
      else -> null
    }
    showDatePickerDialog(date, datePickerListener)
  }

  fun addFamily() {
    memberAdapter.addItem(Member(), 0)
  }
}