package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.ChangeWorkPlaceDialogBinding
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.T5
import com.elka.heofficeclub.service.model.documents.forms.T8
import com.elka.heofficeclub.view.list.divisions.DivisionsSpinnerAdapter
import com.elka.heofficeclub.view.list.organizationPositions.OrgPositionsSpinnerAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.ChangeWorkPlaceViewModel
import com.elka.heofficeclub.viewModel.DismissalEmployerViewModel
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.*

open class DismissalEmployerDialog (
  context: Context, private val owner: BaseFragmentWithDatePicker, val listener: Listener
) : Dialog(context) {
  private lateinit var binding: DismissalEmployerDialogBinding
  private val viewModel: DismissalEmployerViewModel by lazy { ViewModelProvider(owner)[DismissalEmployerViewModel::class.java] }


  private val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> { errors ->
    owner.showErrors(errors, fields)
  }

  private val workObserver = androidx.lifecycle.Observer<List<Work>> {
    setCancelable(it.isEmpty())
  }

  private val actionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.GENERATE_T8 -> generateT8()
      Action.AFTER_DISMISSAL_WORK -> afterDismissalWork()
      else -> Unit
    }
  }

  private fun afterDismissalWork() {
    listener.onDismissal(viewModel.getT8())
    viewModel.clear()
    disagree()
  }

  private fun generateT8() {
    val t8 = viewModel.getT8()
    val uri = DocumentCreator(context).createFormT8(t8)
    viewModel.saveT8(t8, uri)
  }

  private val fields: HashMap<Field, Any> by lazy {
    hashMapOf()
  }

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = ChangeWorkPlaceDialogBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@DismissalEmployerDialog
      viewModel = this@DismissalEmployerDialog.viewModel
      lifecycleOwner = owner
    }
    setContentView(binding.root)
    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

  }

  fun open(organization: Organization, employer: Employer) {
    viewModel.clear()
    viewModel.initData(organization, employer)
    show()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    viewModel.fieldErrors.observe(owner, fieldErrorsObserver)
    viewModel.work.observe(owner, workObserver)
    viewModel.externalAction.observe(owner, actionObserver)

  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)
    viewModel.work.removeObserver(workObserver)
    viewModel.externalAction.removeObserver(actionObserver)

  }

  fun disagree() {
    dismiss()
  }

  fun agree() {
    viewModel.trySave()
  }

  fun showDismissalDatePicker() = showDatePicker(DateType.DISMISSAL_DATE)
  fun showDismissalFoundationDocDatePicker() = showDatePicker(DateType.FOUNDATION_DOC_DISMISSAL_DATE)

  private val datePickerListener =
    object : BaseFragmentWithDatePicker.Companion.DatePickerListener {
      override fun onPick(date: Date) {
        viewModel.saveDate(date)
      }
    }

  private fun showDatePicker(type: DateType) {
    viewModel.setEditTime(type)
    val date = when (type) {
      DateType.DISMISSAL_DATE -> viewModel.dismissalDate.value
      DateType.FOUNDATION_DOC_DISMISSAL_DATE -> viewModel.foundationDocDismissalDate.value
      else -> null
    }
    owner.showDatePickerDialog(date, datePickerListener)
  }


  companion object {
    interface Listener {
      fun onDismissal(t8: T8)
    }
  }
}