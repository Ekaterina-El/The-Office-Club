package com.elka.heofficeclub.view.list.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.elka.heofficeclub.databinding.SpinnerOneLineBinding
import com.elka.heofficeclub.other.documents.Gender

class GendersAdapter(context: Context, private val genders: Array<Gender>) :
  ArrayAdapter<Gender>(context, 0, genders) {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

    val binding: SpinnerOneLineBinding = when (convertView) {
      null -> SpinnerOneLineBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerOneLineBinding
    }

    binding.gender = genders[position]
    binding.root.tag = binding
    return binding.root
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding: SpinnerOneLineBinding = when (convertView) {
      null -> SpinnerOneLineBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerOneLineBinding
    }

    binding.root.tag = binding
    getItem(position)?.let { binding.gender = genders[position] }

    return binding.root
  }
}