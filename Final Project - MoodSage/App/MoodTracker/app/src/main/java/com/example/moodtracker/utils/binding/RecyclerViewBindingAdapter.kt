@file:Suppress("UNCHECKED_CAST")

package com.example.moodtracker.utils.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.data.database.models.entities.Solution
import com.example.moodtracker.data.database.models.entities.Worry

@BindingAdapter("list")
fun RecyclerView.addWorryList(list: List<Worry>?) {
    list?.let {
        val listAdapter = this.adapter as ListAdapter<Worry, *>
        listAdapter.submitList(list)
    }
}

@BindingAdapter("list")
fun RecyclerView.addSolutionList(list: List<Solution>?) {
    list?.let {
        val listAdapter = this.adapter as ListAdapter<Solution, *>
        listAdapter.submitList(list)
    }
}