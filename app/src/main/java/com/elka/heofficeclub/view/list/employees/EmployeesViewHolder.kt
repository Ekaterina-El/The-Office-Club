package com.elka.heofficeclub.view.list.employees

import android.content.Context
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.elka.heofficeclub.databinding.EmployeesItemBinding
import com.elka.heofficeclub.service.model.Employer

class EmployeesViewHolder(
  val context: Context,
  val binding: EmployeesItemBinding,
  val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {
  private var employer: Employer? = null

  private val menu by lazy {
    val popupMenu = PopupMenu(context, binding.wrapper)

    popupMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        else -> Unit
      }
      return@setOnMenuItemClickListener true
    }

    return@lazy popupMenu
  }

  fun bind(employer: Employer) {
    binding.employer = employer
    this.employer = employer

    binding.wrapper.setOnClickListener {
      listener.onSelect(employer)
    }

    binding.wrapper.setOnLongClickListener {
      menu.show()
      return@setOnLongClickListener true
    }
  }


  companion object {

    interface Listener {
      fun onSelect(employer: Employer)
    }
  }
}