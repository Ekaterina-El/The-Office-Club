package com.elka.heofficeclub.view.ui.employerScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.EmployerGeneralInfoFragmentBinding
import com.elka.heofficeclub.other.Field
import com.elka.heofficeclub.other.FieldError
import com.elka.heofficeclub.other.Selector
import com.elka.heofficeclub.other.SpinnerItem
import com.elka.heofficeclub.other.documents.DateType
import com.elka.heofficeclub.other.documents.Gender
import com.elka.heofficeclub.other.documents.getGenderSpinnerItems
import com.elka.heofficeclub.view.list.users.SpinnerAdapter
import com.elka.heofficeclub.view.ui.BaseEmployerFragment
import java.util.*

class EmployerGeneralInfoFragment : BaseEmployerFragment() {
  override val currentScreen: Int = 1
  private lateinit var binding: EmployerGeneralInfoFragmentBinding

  private val fieldErrorsObserver = androidx.lifecycle.Observer<List<FieldError>> {
    showErrors(it, fields)
  }


  fun showRegAccorinigAddressPicker() = showDatePicker(viewModel, DateType.REG_ACCORINING_ADDRESS)
  fun showBirthdatePicker() = showDatePicker(viewModel, DateType.BIRTDAY)
  fun showPassportDatePicker() = showDatePicker(viewModel, DateType.PASSPORT_DATE)

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    binding = EmployerGeneralInfoFragmentBinding.inflate(layoutInflater, container, false)
    binding.apply {
      lifecycleOwner = viewLifecycleOwner
      master = this@EmployerGeneralInfoFragment
      viewModel = this@EmployerGeneralInfoFragment.viewModel
    }

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val spinnerAdapter = SpinnerAdapter(requireContext(), getGenderSpinnerItems())
    binding.genderSpinner.adapter = spinnerAdapter
  }

  private val fields: HashMap<Field, Any> by lazy {
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

  override fun onResume() {
    super.onResume()

    viewModel.error.observe(this, errorObserver)
    viewModel.fieldErrors.observe(this, fieldErrorsObserver)

    binding.genderSpinner.onItemSelectedListener = genderSpinnerListener
  }

  override fun onStop() {
    super.onStop()

    viewModel.error.removeObserver(errorObserver)
    viewModel.fieldErrors.removeObserver(fieldErrorsObserver)

    binding.genderSpinner.onItemSelectedListener = null
  }

  override fun goNext() {
    if (!viewModel.checkFieldsCurrentScreen(currentScreen)) return

    val action = R.id.action_employerGeneralInfoFragment_to_employerEducationFragment
    navigate(action)
  }

  override fun goBack() {
    viewModel.goBack()
  }

  private val genderSpinnerListener by lazy {
    Selector {
      val spinner = it as SpinnerItem
      val gender = spinner.value as Gender
      viewModel.setGender(gender)
    }
  }
}