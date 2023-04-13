package com.elka.heofficeclub.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.databinding.EmployeesBottomSheetDialogBinding
import com.elka.heofficeclub.other.documents.FormType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EmployeesBottomSheetDialog(private val itemListener: ItemClickListener) : BottomSheetDialogFragment(){
  private lateinit var binding: EmployeesBottomSheetDialogBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployeesBottomSheetDialogBinding.inflate(LayoutInflater.from(context))
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.addEmployer.setOnClickListener { itemListener.onItemClick(FormType.T2) }
    binding.createT3.setOnClickListener { itemListener.onItemClick(FormType.T3) }
    binding.createT7.setOnClickListener { itemListener.onItemClick(FormType.T7) }

  }
  companion object {
    interface ItemClickListener {
      fun onItemClick(formType: FormType)
    }
  }
}