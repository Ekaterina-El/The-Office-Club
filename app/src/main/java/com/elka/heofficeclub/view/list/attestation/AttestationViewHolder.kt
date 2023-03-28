package com.elka.heofficeclub.view.list.attestation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.AttestationItemBinding
import com.elka.heofficeclub.other.documents.Attestation

class AttestationViewHolder(
  val context: Context,
  val binding: AttestationItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(attestation: Attestation) {
    binding.attestation = attestation
  }
}