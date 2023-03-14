package com.elka.heofficeclub.view.list.organizationPositions

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
import com.elka.heofficeclub.databinding.SpinnerPositionItemBinding
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.User

class OrgPositionsSpinnerAdapter(context: Context, divisions: List<OrganizationPosition>) :
  ArrayAdapter<OrganizationPosition>(context, 0, divisions) {
  private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

    val binding: SpinnerPositionItemBinding = when (convertView) {
      null -> SpinnerPositionItemBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerPositionItemBinding
    }

    binding.position = getItem(position)
    binding.root.tag = binding
    return binding.root
  }

  override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding: SpinnerPositionItemBinding = when (convertView) {
      null -> SpinnerPositionItemBinding.inflate(layoutInflater)
      else -> convertView.tag as SpinnerPositionItemBinding
    }

    binding.root.tag = binding
    getItem(position)?.let { binding.position = it }

    return binding.root
  }
}