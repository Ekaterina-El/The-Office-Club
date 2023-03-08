package com.elka.heofficeclub.view.list.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DropDownSpinnerItemUserBinding
import com.elka.heofficeclub.databinding.SpinnerItemUserBinding
import com.elka.heofficeclub.service.model.User

class SpinnerUsersAdapter(context: Context, users: List<User>) :
  ArrayAdapter<User>(context, 0, users) {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

    val binding: SpinnerItemUserBinding = when (convertView) {
      null -> SpinnerItemUserBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerItemUserBinding
    }

    binding.user = getItem(position)
    binding.root.tag = binding
    return binding.root
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding: DropDownSpinnerItemUserBinding = when (convertView) {
      null -> DropDownSpinnerItemUserBinding.inflate(layoutInflater)
      else -> convertView.tag as DropDownSpinnerItemUserBinding
    }

    binding.root.tag = binding
    getItem(position)?.let { binding.user = it }

    return binding.root
  }
}