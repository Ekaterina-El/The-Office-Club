package com.elka.heofficeclub.view.list.t7.t7Vacations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.T7ItemVacationBinding
import com.elka.heofficeclub.other.T7RowVacation
import com.elka.heofficeclub.view.list.BaseAdapter

class AdapterT7Vacations : BaseAdapter<T7RowVacation>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = T7ItemVacationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return T7VacationViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as T7VacationViewHolder).bind(item)
  }
}