package com.elka.heofficeclub.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.elka.heofficeclub.databinding.GetGiftDialogBinding
import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Work
import com.elka.heofficeclub.other.documents.DocumentCreator
import com.elka.heofficeclub.other.documents.Gift
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.Organization
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.GetGiftViewModel
import java.util.*

open class GetGiftDialog(context: Context, private val owner: BaseFragmentWithDatePicker, val listener: Listener) :
  Dialog(context) {
  private lateinit var binding: GetGiftDialogBinding
  private val viewModel: GetGiftViewModel by lazy { ViewModelProvider(owner)[GetGiftViewModel::class.java] }

  private val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> { errors ->
    owner.showErrors(errors, fields)
  }

  private val workObserver = androidx.lifecycle.Observer<List<Work>> {
    setCancelable(it.isEmpty())
  }

  private val actionObserver = androidx.lifecycle.Observer<Action?> {
    if (it == null) return@Observer

    when (it) {
      Action.GENERATE_T6 -> generateT6()
      Action.AFTER_GET_GIFT -> getGiftAndBack()
      else -> Unit
    }
  }

  private fun getGiftAndBack() {
    listener.addGift(viewModel.gift!!)
    viewModel.clear()
    disagree()
  }

  private fun generateT6() {
    val t11 = viewModel.getT11()
    val uri = DocumentCreator(context).createFormT11(t11)
    viewModel.saveT11(t11, uri)
  }

  private val fields: HashMap<Field, Any> by lazy {
    hashMapOf(
      Pair(Field.GIFT_DESCRIPTION, binding.layoutGiftDescription),
      Pair(Field.GIFT_TYPE, binding.layoutGiftType),
      Pair(Field.GIFT_REASON, binding.layoutGiftReason),
    )
  }

  init {
    initDialog()
  }

  private fun initDialog() {
    binding = GetGiftDialogBinding.inflate(LayoutInflater.from(context))
    binding.apply {
      master = this@GetGiftDialog
      viewModel = this@GetGiftDialog.viewModel
      lifecycleOwner = owner
    }
    setContentView(binding.root)
    window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
  }


  fun open(organization: Organization?, employer: Employer?) {
    viewModel.clear()
    viewModel.setOrganization(organization)
    viewModel.setEmployer(employer)
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

  companion object {
    interface Listener {
      fun addGift(gift: Gift)
    }
  }
}