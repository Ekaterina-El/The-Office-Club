package com.elka.heofficeclub.view.list.profTrainings

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.ProfTriningItemBinding
import com.elka.heofficeclub.other.documents.ProfTraining

class ProfTrainingsViewHolder(
  val context: Context,
  val binding: ProfTriningItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(profTraining: ProfTraining) {
    binding.profTraining = profTraining
  }
}