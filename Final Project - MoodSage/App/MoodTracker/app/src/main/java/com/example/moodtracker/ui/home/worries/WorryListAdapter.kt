package com.example.moodtracker.ui.home.worries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.databinding.RecyclerWorryLayoutBinding

class WorryListAdapter(private val clickListener: WorryItemClickListener) : ListAdapter<Worry, WorryListAdapter.ViewHolder>(WorryDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: RecyclerWorryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Worry, clickListener: WorryItemClickListener) {
            binding.worry = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerWorryLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class WorryDiffCallback : DiffUtil.ItemCallback<Worry>() {

    override fun areItemsTheSame(oldItem: Worry, newItem: Worry): Boolean =
        oldItem.date == newItem.date

    override fun areContentsTheSame(oldItem: Worry, newItem: Worry): Boolean = oldItem == newItem

}