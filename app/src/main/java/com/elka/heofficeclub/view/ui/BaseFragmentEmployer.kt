package com.elka.heofficeclub.view.ui

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.view.dialog.OrganizationPositionDialog
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.list.users.MemberViewHolder
import com.elka.heofficeclub.view.list.users.MembersAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.viewModel.BaseViewModelEmployer
import com.google.android.material.textfield.TextInputLayout
import java.util.*

abstract class BaseFragmentEmployer : BaseFragmentWithDatePicker() {
  abstract val isCreation: Boolean

  open lateinit var viewModel: BaseViewModelEmployer

  private val datePickerListener = object : Companion.DatePickerListener {
    override fun onPick(date: Date) {
      viewModel.saveDate(date)
    }
  }

  fun showRegAccorinigAddressPicker() = showDatePicker(viewModel, DateType.REG_ACCORINING_ADDRESS)
  fun showBirthdatePicker() = showDatePicker(viewModel, DateType.BIRTDAY)
  fun showPassportDatePicker() = showDatePicker(viewModel, DateType.PASSPORT_DATE)
  fun showContractDatePicker() =
    if (isCreation) showDatePicker(viewModel, DateType.CONTRACT) else Unit

  fun showStartWorkDatePicker() =
    if (isCreation) showDatePicker(viewModel, DateType.START_WORK) else Unit

  fun showEndWorkDatePicker() =
    if (isCreation) showDatePicker(viewModel, DateType.END_WORK) else Unit

  private fun showDatePicker(viewModel: BaseViewModelEmployer, type: DateType) {
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

  abstract val fields: HashMap<Field, Any>
  val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> {
    showErrors(it)
  }

  protected fun showErrors(errors: List<FieldError>?) {
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

  protected val memberAdapter: MembersAdapter by lazy { MembersAdapter(memberListener) }
  private val memberListener by lazy {
    object : MemberViewHolder.Companion.Listener {
      override fun onDelete(pos: Int) {
        memberAdapter.removeByPos(pos)
      }
    }
  }

  override val externalActionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.CREATE_FILE_T1 -> {
        val t1 = viewModel.newT1 ?: return@Observer
        val uri = DocumentCreator(requireContext()).createFormT1(t1)

        viewModel.saveT1(t1, uri)
      }

      Action.GO_BACK -> {
        goBack()
      }

      else -> Unit
    }
  }

  abstract var positionSpinner: Spinner?
  abstract var genderSpinner: Spinner
  abstract var educationTypeSpinner: Spinner
  abstract var postgEducationTypeSpinner: Spinner
  abstract var merriedStatusSpinner: Spinner
  abstract var divisionsSpinner: Spinner?

  abstract var recyclerViewMembers: RecyclerView

  private val divisionsObserver = androidx.lifecycle.Observer<List<Division>> {
    val spinnerAdapter = DivisionsSpinnerAdapter(requireContext(), it)
    divisionsSpinner?.adapter = spinnerAdapter
  }

  private val positionsObserver = androidx.lifecycle.Observer<List<OrganizationPosition>> {
    val items = it.toMutableList()
    items.add(
      OrganizationPosition(
        id = Constants.RESERVED_TO_ADD,
        name = getString(R.string.add_position)
      )
    )
    val spinnerAdapter = OrgPositionsSpinnerAdapter(requireContext(), items)
    positionSpinner?.adapter = spinnerAdapter
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initSpinners()

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    recyclerViewMembers.addItemDecoration(decorator)
  }

  private fun initSpinners() {
    val spinnerAdapter = SpinnerAdapter(requireContext(), getGenderSpinnerItems())
    genderSpinner.adapter = spinnerAdapter

    val educationsAdapter = SpinnerAdapter(requireContext(), getEducationSpinnerItems())
    educationTypeSpinner.adapter = educationsAdapter

    val postgEducationsAdapter =
      SpinnerAdapter(requireContext(), getPostgraduateEducationSpinnerItems())
    postgEducationTypeSpinner.adapter = postgEducationsAdapter

    val marriedStatusAdapter = SpinnerAdapter(requireContext(), getMarriedStatusSpinnerItems())
    merriedStatusSpinner.adapter = marriedStatusAdapter
  }

  fun goNextScreen() {
    if (viewModel.screen.value == 3) viewModel.setFamilyMembers(memberAdapter.getMembers())
    viewModel.goNextScreen()
  }

  fun goBack() {
    memberAdapter.clear()
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
      if (position.id == Constants.RESERVED_TO_ADD) {
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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    organizationViewModel.setBottomMenuStatus(false)
    activity.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        viewModel.goPrevScreen()
      }
    })
  }

  override fun onResume() {
    super.onResume()

    viewModel.error.observe(this, errorObserver)
    viewModel.fieldErrors.observe(this, fieldErrorsObserver)
    viewModel.externalAction.observe(this, externalActionObserver)
    viewModel.divisions.observe(this, divisionsObserver)
    viewModel.positions.observe(this, positionsObserver)
    viewModel.work.observe(this, workObserver)

    genderSpinner.onItemSelectedListener = genderSpinnerListener
    educationTypeSpinner.onItemSelectedListener = educationSpinnerListener
    postgEducationTypeSpinner.onItemSelectedListener = postgEducationSpinnerListener
    merriedStatusSpinner.onItemSelectedListener = marriedSpinnerListener
    divisionsSpinner?.onItemSelectedListener = divisionSpinnerListener
    positionSpinner?.onItemSelectedListener = positionSpinnerListener
  }

  override fun onStop() {
    super.onStop()
    viewModel.error.removeObserver(errorObserver)
    viewModel.externalAction.removeObserver(externalActionObserver)
    viewModel.divisions.removeObserver(divisionsObserver)
    viewModel.positions.removeObserver(positionsObserver)
    viewModel.work.removeObserver(workObserver)

    genderSpinner.onItemSelectedListener = null
    educationTypeSpinner.onItemSelectedListener = null
    postgEducationTypeSpinner.onItemSelectedListener = null
    merriedStatusSpinner.onItemSelectedListener = null
    divisionsSpinner?.onItemSelectedListener = null
    positionSpinner?.onItemSelectedListener = null
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

  fun addFamily() {
    memberAdapter.addItem(Member(), 0)
  }
}