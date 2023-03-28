package com.elka.heofficeclub.view.list.profTrainings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.ProfTriningItemBinding
import com.elka.heofficeclub.other.documents.ProfTraining
import com.elka.heofficeclub.view.list.BaseAdapter

class ProfTrainingsAdapter : BaseAdapter<ProfTraining>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = ProfTriningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ProfTrainingsViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as ProfTrainingsViewHolder).bind(item)
  }
}