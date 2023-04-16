package com.elka.heofficeclub.view.list.t7

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.T7ItemBinding
import com.elka.heofficeclub.other.T7Row
import com.elka.heofficeclub.view.list.t7.t7Vacations.AdapterT7Vacations

class T7ViewHolder(
  val context: Context,
  val binding: T7ItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(row: T7Row) {
    binding.t7Row = row

    val adapter = AdapterT7Vacations()
    adapter.setItems(row.vacations)
    binding.list.adapter = adapter
  }
}