package com.elka.heofficeclub.view.list.divisions

import android.content.Context
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DivisionItemBinding
import com.elka.heofficeclub.service.model.Division

class DivisionsViewHolder(val context: Context, val binding: DivisionItemBinding, val listener: DivisionsAdapter.Companion.Listener): RecyclerView.ViewHolder(binding.root) {
  private var division: Division? = null

  private val menu by lazy {
    val popupMenu = PopupMenu(context, binding.wrapper)
    popupMenu.menu.add(0, DELETE_DIVISION, 0, R.string.delete)
    popupMenu.show()

    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        DELETE_DIVISION -> listener.onDelete(division!!)
        else -> Unit
      }
      return@setOnMenuItemClickListener true
    }

    return@lazy popupMenu
  }

  fun bind(division: Division) {
    binding.division = division
    this.division = division

    binding.wrapper.setOnLongClickListener {
      menu.show()
      return@setOnLongClickListener true
    }

    binding.wrapper.setOnClickListener { listener.onSelect(division) }
  }

  companion object {
    const val DELETE_DIVISION = 1
  }
}