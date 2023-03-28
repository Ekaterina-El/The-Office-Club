package com.elka.heofficeclub.view.list.attestation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.AttestationItemBinding
import com.elka.heofficeclub.databinding.WorkItemBinding
import com.elka.heofficeclub.other.documents.Attestation
import com.elka.heofficeclub.other.documents.WorkExperience
import com.elka.heofficeclub.view.list.BaseAdapter

class AttestationsAdapter : BaseAdapter<Attestation>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = AttestationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AttestationViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as AttestationViewHolder).bind(item)
  }
}