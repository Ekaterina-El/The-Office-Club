package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerFamilyFragmentBinding
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.MaritalStatus
import com.elka.heofficeclub.other.documents.Member
import com.elka.heofficeclub.other.documents.getMarriedStatusSpinnerItems
import com.elka.heofficeclub.view.list.users.MemberViewHolder
import com.elka.heofficeclub.view.list.users.MembersAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseEmployerFragment

class EmployerFamilyFragment : BaseEmployerFragment() {
  override val currentScreen: Int = 3
  private lateinit var binding: EmployerFamilyFragmentBinding

  private val membersAdapter: MembersAdapter by lazy {
    MembersAdapter(
      membersListener, viewModel.employer.value!!.T8Local != null
    )
  }
  private val membersListener by lazy {
    object : MemberViewHolder.Companion.Listener {
      override fun onDelete(pos: Int) {
        membersAdapter.removeByPos(pos)
      }
    }
  }
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

  private val marriedStatuses by lazy { getMarriedStatusSpinnerItems() }
  private val marriedStatusAdapter by lazy {
    SpinnerAdapter(requireContext(), getMarriedStatusSpinnerItems())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.merriedStatusSpinner.adapter = marriedStatusAdapter

    val marriedStatus = viewModel.marriedStatus
    if (marriedStatus != null) {
      val pos = marriedStatuses.indexOfFirst { it.value == marriedStatus }
      binding.merriedStatusSpinner.setSelection(pos)
    }

    membersAdapter.setItems(viewModel.members)

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

  override fun goNext() {
    viewModel.setFamilyMembers(membersAdapter.getMembers())

    val action = R.id.action_employerFamilyFragment_to_employerMilitaryRegistrationFragment
    navigate(action)
  }

  override fun goBack() {
    val action = R.id.action_employerFamilyFragment_to_employerEducationFragment
    navigate(action)
  }
}