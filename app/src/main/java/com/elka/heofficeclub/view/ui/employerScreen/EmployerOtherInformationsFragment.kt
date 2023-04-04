package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerEducationFragmentBinding
import com.elka.heofficeclub.databinding.EmployerFamilyFragmentBinding
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.databinding.EmployerGeneralInfoFragmentBinding
import com.elka.heofficeclub.databinding.EmployerOtherInformationsFragmentBinding
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.*
import com.elka.heofficeclub.view.dialog.GetGiftDialog
import com.elka.heofficeclub.view.dialog.GetVacationDialog
import com.elka.heofficeclub.view.list.advanceTrainings.AdvanceTrainingsAdapter
import com.elka.heofficeclub.view.list.attestation.AttestationsAdapter
import com.elka.heofficeclub.view.list.gifts.GiftsAdapter
import com.elka.heofficeclub.view.list.profTrainings.ProfTrainingsAdapter
import com.elka.heofficeclub.view.list.socialBenefits.SocialBenefitAdapter
import com.elka.heofficeclub.view.list.users.MemberViewHolder
import com.elka.heofficeclub.view.list.users.MembersAdapter
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.list.vacations.VacationsAdapter
import com.elka.heofficeclub.view.list.works.WorksAdapter
import com.elka.heofficeclub.view.ui.BaseEmployerFragment
import com.elka.heofficeclub.view.ui.BaseFragmentWithDatePicker
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.HashMap

class EmployerOtherInformationsFragment : BaseEmployerFragment() {
  override val currentScreen: Int = 5
  private lateinit var binding: EmployerOtherInformationsFragmentBinding


  private val worksAdapter: WorksAdapter by lazy { WorksAdapter() }
  private val worksObserver = Observer<List<WorkExperience>> {
    worksAdapter.setItems(it)
  }

  private val attestationsAdapter by lazy { AttestationsAdapter() }
  private val attestationsObserver = Observer<List<Attestation>> {
    attestationsAdapter.setItems(it)
  }

  private val advanceTrainingsAdapter by lazy { AdvanceTrainingsAdapter() }
  private val advanceTrainingsObserver = Observer<List<AdvanceTraining>> {
    advanceTrainingsAdapter.setItems(it)
  }

  private val profTrainingsAdapter by lazy { ProfTrainingsAdapter() }
  private val profTrainingsObserver = Observer<List<ProfTraining>> {
    profTrainingsAdapter.setItems(it)
  }

  private val giftsAdapter by lazy { GiftsAdapter() }
  private val giftsObserver = Observer<List<Gift>> {
    giftsAdapter.setItems(it)
  }

  private val vacationsAdapter by lazy { VacationsAdapter() }
  private val vacationsObserver = Observer<List<Vacation>> {
    vacationsAdapter.setItems(it)
  }

  private val socialBenefitsAdapter by lazy { SocialBenefitAdapter() }
  private val socialBenefitsObserver = Observer<List<SocialBenefit>> {
    socialBenefitsAdapter.setItems(it)
  }


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = EmployerOtherInformationsFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerOtherInformationsFragment
      viewModel = this@EmployerOtherInformationsFragment.viewModel

      worksAdapter = this@EmployerOtherInformationsFragment.worksAdapter
      attestationsAdapter = this@EmployerOtherInformationsFragment.attestationsAdapter
      advanceTrainingsAdapter = this@EmployerOtherInformationsFragment.advanceTrainingsAdapter
      profTrainingsAdapter = this@EmployerOtherInformationsFragment.profTrainingsAdapter
      giftsAdapter = this@EmployerOtherInformationsFragment.giftsAdapter
      vacationsAdapter = this@EmployerOtherInformationsFragment.vacationsAdapter
      socialBenefitsAdapter = this@EmployerOtherInformationsFragment.socialBenefitsAdapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
    val dataNoFound = getString(R.string.data_no_found)

    binding.worksList.addItemDecoration(decorator)

    binding.attestationList.addItemDecoration(decorator)
    binding.noFoundAttestations.message.text = dataNoFound

    binding.advanceTrainingList.addItemDecoration(decorator)
    binding.noFoundAdvanceTrainings.message.text = dataNoFound

    binding.profTrainingList.addItemDecoration(decorator)
    binding.noFoundProfTrainings.message.text = dataNoFound

    binding.giftsList.addItemDecoration(decorator)
    binding.noFoundGifts.message.text = dataNoFound

    binding.vacationsList.addItemDecoration(decorator)
    binding.noFoundVacations.message.text = dataNoFound

    binding.socialBenefitsList.addItemDecoration(decorator)
    binding.noFoundSocialBenefits.message.text = dataNoFound
  }

  override fun onResume() {
    super.onResume()

    viewModel.works.observe(viewLifecycleOwner, worksObserver)
    viewModel.attestation.observe(viewLifecycleOwner, attestationsObserver)
    viewModel.advanceTraining.observe(viewLifecycleOwner, advanceTrainingsObserver)
    viewModel.profTraining.observe(viewLifecycleOwner, profTrainingsObserver)
    viewModel.gifts.observe(viewLifecycleOwner, giftsObserver)
    viewModel.vocations.observe(viewLifecycleOwner, vacationsObserver)
    viewModel.socialBenefits.observe(viewLifecycleOwner, socialBenefitsObserver)
  }

  override fun onStop() {
    super.onStop()

    viewModel.works.removeObserver(worksObserver)
    viewModel.attestation.removeObserver(attestationsObserver)
    viewModel.advanceTraining.removeObserver(advanceTrainingsObserver)
    viewModel.profTraining.removeObserver(profTrainingsObserver)
    viewModel.gifts.removeObserver(giftsObserver)
    viewModel.vocations.removeObserver(vacationsObserver)
    viewModel.socialBenefits.removeObserver(socialBenefitsObserver)
  }

  private val getVacationListener by lazy {
    object: GetVacationDialog.Companion.Listener {
      override fun addVacations(vacations: List<Vacation>) {
        viewModel.addVacations(vacations)
      }
    }
  }

  private val getVacationDialog by lazy { GetVacationDialog(requireContext(), this, getVacationListener) }
  fun addVacation() {
    getVacationDialog.open(organizationViewModel.organization.value, viewModel.employer.value)
  }



  private val getGiftListener by lazy {
    object: GetGiftDialog.Companion.Listener {
      override fun addGift(gift: Gift) {
        viewModel.addGift(gift)
      }
    }
  }

  private val getGiftDialog by lazy { GetGiftDialog(requireContext(), this, getGiftListener) }
  fun addGits() {
    getGiftDialog.open(organizationViewModel.organization.value, viewModel.employer.value)
  }
}