package com.elka.heofficeclub.view.list.t3

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.GiftItemBinding
import com.elka.heofficeclub.databinding.T3ItemBinding
import com.elka.heofficeclub.other.T3Row
import com.elka.heofficeclub.other.documents.Gift

class T3ViewHolder(
  val context: Context,
  val binding: T3ItemBinding
) : RecyclerView.ViewHolder(binding.root) {
  fun bind(row: T3Row) {
    binding.t3Row = row
  }
}