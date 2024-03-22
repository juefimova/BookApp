package com.example.loginapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.RoomOpenHelper
import com.example.loginapp.databinding.ItemNoteBinding
import com.example.loginapp.model.RoomBook


class NotesAdapter(
    private var items: MutableList<RoomBook>,
    private val itemClick: (RoomBook) -> Unit
) : RecyclerView.Adapter<NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NotesViewHolder(
            binding = ItemNoteBinding.inflate(layoutInflater, parent, false),
            itemClick
        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    fun filterList(filteredList: ArrayList<RoomBook>) {
        items = filteredList
        notifyDataSetChanged()
    }


}

class NotesViewHolder(
    private val binding: ItemNoteBinding,
    private val itemClick: (RoomBook) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: RoomBook, position: Int) {
        binding.run {
            title.text = item.title
            image.setImageResource(item.image)
            root.setOnClickListener {
                itemClick(item)
            }

        }

    }
}