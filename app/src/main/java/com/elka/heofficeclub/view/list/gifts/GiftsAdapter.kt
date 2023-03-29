package com.elka.heofficeclub.view.list.gifts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.GiftItemBinding
import com.elka.heofficeclub.other.documents.Gift
import com.elka.heofficeclub.view.list.BaseAdapter

class GiftsAdapter : BaseAdapter<Gift>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding = GiftItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return GiftsViewHolder(parent.context, binding)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val item = items[position]
    (holder as GiftsViewHolder).bind(item)
  }
}