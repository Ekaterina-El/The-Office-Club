package com.elka.heofficeclub.view.list.divisions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.DivisionItemBinding
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.view.list.BaseAdapter

class DivisionsAdapter: BaseAdapter<Division>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = DivisionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DivisionsViewHolder(binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val division = items[position]
    (holder as DivisionsViewHolder).bind(division)
  }
}