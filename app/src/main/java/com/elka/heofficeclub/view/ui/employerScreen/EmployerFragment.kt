package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerFragmentBinding
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.documents.AdvanceTraining
import com.elka.heofficeclub.other.documents.Attestation
import com.elka.heofficeclub.other.documents.WorkExperience
import com.elka.heofficeclub.view.list.advanceTrainings.AdvanceTrainingsAdapter
import com.elka.heofficeclub.view.list.attestation.AttestationsAdapter
import com.elka.heofficeclub.view.list.works.WorksAdapter
import com.elka.heofficeclub.view.ui.BaseFragmentEmployer
import com.elka.heofficeclub.viewModel.BaseViewModelEmployer
import com.elka.heofficeclub.viewModel.EmployerViewModel
import java.util.Calendar
import java.util.HashMap

class EmployerFragment : BaseFragmentEmployer() {
  override val isCreation = false
  private lateinit var binding: EmployerFragmentBinding
  override lateinit var viewModel: BaseViewModelEmployer

  private lateinit var worksAdapter: WorksAdapter
  private val worksObserver = Observer<List<WorkExperience>> {
    worksAdapter.setItems(it)
  }

  private lateinit var attestationsAdapter: AttestationsAdapter
  private val attestationsObserver = Observer<List<Attestation>> {
    attestationsAdapter.setItems(it)
  }
  private lateinit var advanceTrainingsAdapter: AdvanceTrainingsAdapter
  private val advanceTrainingsObserver = Observer<List<AdvanceTraining>> {
    advanceTrainingsAdapter.setItems(it)
  }

  override var positionSpinner: Spinner? = null
  override lateinit var genderSpinner: Spinner
  override lateinit var educationTypeSpinner: Spinner
  override lateinit var postgEducationTypeSpinner: Spinner
  override lateinit var merriedStatusSpinner: Spinner
  override var divisionsSpinner: Spinner? = null
  override lateinit var recyclerViewMembers: RecyclerView

  override val fields: HashMap<Field, Any> by lazy {
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
    )
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    worksAdapter = WorksAdapter()
    attestationsAdapter = AttestationsAdapter()
    advanceTrainingsAdapter = AdvanceTrainingsAdapter()

    viewModel = ViewModelProvider(this)[EmployerViewModel::class.java]
    binding = EmployerFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerFragment
      viewModel = this@EmployerFragment.viewModel as EmployerViewModel
      worksAdapter = this@EmployerFragment.worksAdapter
      attestationsAdapter = this@EmployerFragment.attestationsAdapter
      advanceTrainingsAdapter = this@EmployerFragment.advanceTrainingsAdapter
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    genderSpinner = binding.genderSpinner
    educationTypeSpinner = binding.educationTypeSpinner
    postgEducationTypeSpinner = binding.postgEducationTypeSpinner
    merriedStatusSpinner = binding.merriedStatusSpinner
    recyclerViewMembers = binding.recyclerViewMembers

    super.onViewCreated(view, savedInstanceState)

    val args = EmployerFragmentArgs.fromBundle(requireArguments())
    viewModel.setEmployer(args.employer)

    val decorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)

    binding.worksList.addItemDecoration(decorator)

    binding.attestationList.addItemDecoration(decorator)
    binding.noFoundAttestations.message.text = getString(R.string.data_no_found)

    binding.advanceTrainingList.addItemDecoration(decorator)
    binding.noFoundAdvanceTrainings.message.text = getString(R.string.data_no_found)

  }

  override fun onResume() {
    super.onResume()
    viewModel.works.observe(viewLifecycleOwner, worksObserver)
    viewModel.attestation.observe(viewLifecycleOwner, attestationsObserver)
    viewModel.advanceTraining.observe(viewLifecycleOwner, advanceTrainingsObserver)
  }

  override fun onStop() {
    super.onStop()
    viewModel.works.removeObserver(worksObserver)
    viewModel.attestation.removeObserver(attestationsObserver)
    viewModel.advanceTraining.removeObserver(advanceTrainingsObserver)
  }
}