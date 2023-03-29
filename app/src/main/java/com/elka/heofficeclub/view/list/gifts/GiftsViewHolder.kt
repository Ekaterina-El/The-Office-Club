package com.elka.heofficeclub.view.list.gifts

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.GiftItemBinding
import com.elka.heofficeclub.other.documents.Gift

class GiftsViewHolder(
  val context: Context,
  val binding: GiftItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(gift: Gift) {
    binding.gift = gift
  }
}