package com.elka.heofficeclub.view.ui.documentCreators

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.viewModel.CreateEmployerViewModel
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.databinding.CreateEmployerBinding
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.view.list.users.MemberViewHolder
import com.elka.heofficeclub.view.list.users.MembersAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter

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

  private val positionsObserver = Observer<List<OrganizationPosition>> {
    val spinnerAdapter = OrgPositionsSpinnerAdapter(requireContext(), it)
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

        // change t1 (work, nature of work, condition of work)
        Toast.makeText(requireContext(), "Create T1!", Toast.LENGTH_SHORT).show()
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

  private fun showErrors(errors: List<FieldError>?) {}

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
    Selector { viewModel.selectPosition(it as OrganizationPosition) }
  }

  override fun onResume() {
    super.onResume()


    viewModel.error.observe(this, errorObserver)
    viewModel.externalAction.observe(this, externalActionObserver)
    viewModel.divisions.observe(this, divisionsObserver)
    viewModel.positions.observe(this, positionsObserver)

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