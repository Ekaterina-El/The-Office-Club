package com.elka.heofficeclub.view.list.vacations

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.GiftItemBinding
import com.elka.heofficeclub.databinding.VacationItemBinding
import com.elka.heofficeclub.other.documents.Gift
import com.elka.heofficeclub.other.documents.Vacation

class VacationsViewHolder(val context: Context, val binding: VacationItemBinding) :
  RecyclerView.ViewHolder(binding.root) {
  fun bind(vacation: Vacation) {
    binding.vacation = vacation
  }
}