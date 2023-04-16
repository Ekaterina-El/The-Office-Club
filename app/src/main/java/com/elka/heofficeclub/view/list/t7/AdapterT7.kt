package com.elka.heofficeclub.view.list.t7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.T7ItemBinding
import com.elka.heofficeclub.other.T7Row
import com.elka.heofficeclub.view.list.BaseAdapter

class AdapterT7 : BaseAdapter<T7Row>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = T7ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return T7ViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as T7ViewHolder).bind(item)
  }
}