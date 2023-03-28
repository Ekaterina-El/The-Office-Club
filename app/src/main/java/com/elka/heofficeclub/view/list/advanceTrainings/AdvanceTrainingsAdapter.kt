package com.elka.heofficeclub.view.list.advanceTrainings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.AdvanceTriningItemBinding
import com.elka.heofficeclub.other.documents.AdvanceTraining
import com.elka.heofficeclub.view.list.BaseAdapter
import com.elka.heofficeclub.view.list.attestation.AdvanceTrainingViewHolder

class AdvanceTrainingsAdapter : BaseAdapter<AdvanceTraining>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = AdvanceTriningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return AdvanceTrainingViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as AdvanceTrainingViewHolder).bind(item)
  }
}