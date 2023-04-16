package com.elka.heofficeclub.view.list.t7.t7Vacations

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.T7ItemVacationBinding
import com.elka.heofficeclub.other.T7RowVacation

class T7VacationViewHolder(
  val context: Context,
  val binding: T7ItemVacationBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(vacation: T7RowVacation) {
    binding.t7RowVacation = vacation
  }
}