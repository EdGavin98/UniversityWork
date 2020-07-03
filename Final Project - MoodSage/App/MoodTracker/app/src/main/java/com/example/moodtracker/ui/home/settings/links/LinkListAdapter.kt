package com.example.moodtracker.ui.home.settings.links

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.databinding.RecyclerLinkLayoutBinding

class LinkListAdapter(private val clickListener: LinkButtonClickListener) :
    ListAdapter<LinkDto, LinkListAdapter.ViewHolder>(LinkDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: RecyclerLinkLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LinkDto, clickListener: LinkButtonClickListener) {
            binding.link = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerLinkLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class LinkDiffCallback : DiffUtil.ItemCallback<LinkDto>() {

    override fun areItemsTheSame(oldItem: LinkDto, newItem: LinkDto): Boolean =
        oldItem.user.id == newItem.user.id

    override fun areContentsTheSame(oldItem: LinkDto, newItem: LinkDto): Boolean =
        oldItem == newItem

}