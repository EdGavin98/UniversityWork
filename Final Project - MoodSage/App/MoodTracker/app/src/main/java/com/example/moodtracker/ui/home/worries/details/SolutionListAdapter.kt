package com.example.moodtracker.ui.home.worries.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.databinding.RecyclerSolutionLayoutBinding

class SolutionListAdapter() : ListAdapter<Solution, SolutionListAdapter.ViewHolder>(SolutionDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: RecyclerSolutionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Solution) {
            binding.solution = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerSolutionLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SolutionDiffCallback : DiffUtil.ItemCallback<Solution>() {

    override fun areItemsTheSame(oldItem: Solution, newItem: Solution): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Solution, newItem: Solution): Boolean = oldItem == newItem

}