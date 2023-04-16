package com.elka.heofficeclub.view.list.t3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.T3ItemBinding
import com.elka.heofficeclub.other.T3Row
import com.elka.heofficeclub.view.list.BaseAdapter

class AdapterT3 : BaseAdapter<T3Row>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = T3ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return T3ViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as T3ViewHolder).bind(item)
  }
}