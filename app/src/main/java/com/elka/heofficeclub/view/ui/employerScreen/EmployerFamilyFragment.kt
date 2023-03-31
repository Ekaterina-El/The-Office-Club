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

class EmployerFamilyFragment : BaseFragmentWithDatePicker() {
  private lateinit var binding: EmployerFamilyFragmentBinding
  private val viewModel by activityViewModels<EmployerViewModel>()

  private val membersAdapter: MembersAdapter by lazy { MembersAdapter(membersListener) }
  private val membersListener by lazy {
    object : MemberViewHolder.Companion.Listener {
      override fun onDelete(pos: Int) {
        membersAdapter.removeByPos(pos)
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployerFamilyFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerFamilyFragment
      viewModel = this@EmployerFamilyFragment.viewModel
      membersAdapter = this@EmployerFamilyFragment.membersAdapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val marriedStatusAdapter = SpinnerAdapter(requireContext(), getMarriedStatusSpinnerItems())
    binding.merriedStatusSpinner.adapter = marriedStatusAdapter

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    binding.recyclerViewMembers.addItemDecoration(decorator)
  }

  override fun onResume() {
    super.onResume()

    viewModel.error.observe(this, errorObserver)

    binding.merriedStatusSpinner.onItemSelectedListener = marriedSpinnerListener
  }

  override fun onStop() {
    super.onStop()

    viewModel.error.removeObserver(errorObserver)

    binding.merriedStatusSpinner.onItemSelectedListener = null
  }

  private val marriedSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val marriedStatus = spinner.value as MaritalStatus
      viewModel.setMarriedStatus(marriedStatus)
    }
  }

  fun addFamily() {
    membersAdapter.addItem(Member(), 0)
  }

}