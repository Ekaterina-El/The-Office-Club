package com.elka.heofficeclub.view.ui

import android.app.DatePickerDialog
import android.content.DialogInterface
import java.util.*

abstract class BaseFragmentWithDatePicker: BaseFragmentWithOrganization() {

  private var datePickerListener: DatePickerListener? = null
  private var isOkayClicked = false

  private val datePickerDialog: DatePickerDialog by lazy {
    val calendar = Calendar.getInstance()

    val datePickerListener =
      DatePickerDialog.OnDateSetListener { _, year, m, day ->
        if (isOkayClicked) {
          calendar.set(year, m, day)
          val date = calendar.time
          datePickerListener?.onPick(date)
          isOkayClicked = false
        }

      }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(requireContext(), datePickerListener, year, month, day)

    datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, which ->
      if (which == DialogInterface.BUTTON_POSITIVE) {
        isOkayClicked = true
        val datePicker = datePickerDialog.datePicker
        datePickerListener.onDateSet(
          datePicker,
          datePicker.year,
          datePicker.month,
          datePicker.dayOfMonth
        )
      }
    }

    return@lazy datePickerDialog
  }

  fun showDatePickerDialog(date: Date? = null, listener: DatePickerListener?) {
    updateDatePickerDate(date)

    datePickerListener = listener
    datePickerDialog.show()
  }

  private fun updateDatePickerDate(date: Date?) {
    val cal = Calendar.getInstance()
    if (date != null) cal.time = date

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    datePickerDialog.datePicker.updateDate(year, month, day)
  }

  companion object {
    interface DatePickerListener {
      fun onPick(date: Date)
    }
  }
}