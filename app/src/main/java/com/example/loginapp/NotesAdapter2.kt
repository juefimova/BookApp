package com.example.loginapp

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loginapp.databinding.ItemNoteAddressBinding
import com.example.loginapp.model.RoomBook
import com.example.loginapp.model.RoomPhone


class NotesAdapter2 (
    private var items: MutableList<RoomPhone>,
    var itemClick: (RoomPhone) -> Unit
) : RecyclerView.Adapter<NotesViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder2 {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NotesViewHolder2(
            binding = ItemNoteAddressBinding.inflate(layoutInflater, parent, false),
            itemClick
        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder2, position: Int) {
        holder.bind(items[position], position)
    }

    /*fun filterList(filteredList: ArrayList<RoomPhone>) {
        items = filteredList
        notifyDataSetChanged()
    }*/


}



class NotesViewHolder2(
    private val binding: ItemNoteAddressBinding,
    private val itemClick: (RoomPhone) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RoomPhone, position: Int) {
        binding.run {
            number.text = item.phone
            address.text = item.address
            call.setOnClickListener{
                itemClick(item)
                Log.d("call", "попали в call")
            }
            root.setOnClickListener {
            }

        }

    }
}