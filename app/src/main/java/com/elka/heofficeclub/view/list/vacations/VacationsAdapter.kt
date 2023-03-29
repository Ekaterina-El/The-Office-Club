package com.elka.heofficeclub.view.list.vacations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.VacationItemBinding
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.view.list.BaseAdapter

class VacationsAdapter : BaseAdapter<Vacation>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = VacationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return VacationsViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as VacationsViewHolder).bind(item)
  }
}