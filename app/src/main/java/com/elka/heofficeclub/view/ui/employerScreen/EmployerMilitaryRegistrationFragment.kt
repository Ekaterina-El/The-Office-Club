package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.databinding.EmployerEducationFragmentBinding
import com.elka.heofficeclub.databinding.EmployerFamilyFragmentBinding
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.databinding.EmployerGeneralInfoFragmentBinding
import com.elka.heofficeclub.databinding.EmployerMilitaryRegistrationBinding
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.view.list.users.MemberViewHolder
import com.elka.heofficeclub.view.list.users.MembersAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.HashMap

class EmployerMilitaryRegistrationFragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: EmployerMilitaryRegistrationBinding
  private val viewModel by activityViewModels<EmployerViewModel>()


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployerMilitaryRegistrationBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerMilitaryRegistrationFragment
      viewModel = this@EmployerMilitaryRegistrationFragment.viewModel
    }

    return binding.root
  }
}