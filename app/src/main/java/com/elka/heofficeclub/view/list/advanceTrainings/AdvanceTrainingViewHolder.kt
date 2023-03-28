package com.elka.heofficeclub.view.list.attestation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.AdvanceTriningItemBinding
import com.elka.heofficeclub.databinding.AttestationItemBinding
import com.elka.heofficeclub.other.documents.AdvanceTraining
import com.elka.heofficeclub.other.documents.Attestation

class AdvanceTrainingViewHolder(
  val context: Context,
  val binding: AdvanceTriningItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(advanceTraining: AdvanceTraining) {
    binding.advanceTraining = advanceTraining
  }
}