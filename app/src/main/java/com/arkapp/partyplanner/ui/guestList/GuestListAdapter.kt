package com.arkapp.partyplanner.ui.guestList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.CheckedItem

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

class GuestListAdapter(private val guestList: List<CheckedItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GuestListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.rv_guest_list,
                parent,
                false
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as GuestListViewHolder).binding

        val guest = guestList[position]
        binding.guestEt.setText(guest.itemName)
        binding.guestCb.isChecked = guest.selected

        binding.guestCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (binding.guestEt.text!!.isEmpty()) {
                binding.guestName.error = "Required!"
                buttonView.isChecked = false
            } else {
                guestList[position].selected = isChecked
            }
        }

        binding.guestEt.doAfterTextChanged {
            guestList[position].itemName = it.toString()
        }
    }


    override fun getItemCount() = guestList.size

    override fun getItemId(position: Int): Long {
        return guestList[position].hashCode().toLong()
    }

}