package com.elka.heofficeclub.view.list.divisions

import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.DivisionItemBinding
import com.elka.heofficeclub.service.model.Division

class DivisionsViewHolder(val binding: DivisionItemBinding): RecyclerView.ViewHolder(binding.root) {
  fun bind(division: Division) {
    binding.division = division
  }
}