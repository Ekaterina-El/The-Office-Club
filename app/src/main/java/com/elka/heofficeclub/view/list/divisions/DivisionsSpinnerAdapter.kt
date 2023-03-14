package com.elka.heofficeclub.view.list.divisions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.elka.heofficeclub.R
import com.elka.heofficeclub.databinding.DropDownSpinnerItemUserBinding
import com.elka.heofficeclub.databinding.SpinnerDivisionItemBinding
import com.elka.heofficeclub.databinding.SpinnerItemUserBinding
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.User

class DivisionsSpinnerAdapter(context: Context, divisions: List<Division>) :
  ArrayAdapter<Division>(context, 0, divisions) {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

    val binding: SpinnerDivisionItemBinding = when (convertView) {
      null -> SpinnerDivisionItemBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerDivisionItemBinding
    }

    binding.division = getItem(position)
    binding.root.tag = binding
    return binding.root
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding: SpinnerDivisionItemBinding = when (convertView) {
      null -> SpinnerDivisionItemBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerDivisionItemBinding
    }

    binding.root.tag = binding
    getItem(position)?.let { binding.division = it }

    return binding.root
  }
}