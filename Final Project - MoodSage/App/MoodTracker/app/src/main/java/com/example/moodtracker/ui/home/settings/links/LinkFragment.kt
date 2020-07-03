package com.example.moodtracker.ui.home.settings.links

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moodtracker.R
import com.example.moodtracker.databinding.FragmentLinkBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import javax.inject.Inject

class LinkFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LinkViewModel by viewModels { viewModelFactory }

    private lateinit var binding: FragmentLinkBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as HomeActivity).homeComponent.inject(this)
        binding = DataBindingUtil.inflate<FragmentLinkBinding>(
            inflater,
            R.layout.fragment_link,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        initObservers()
        return binding.root
    }

    private fun initObservers() {
        val adapter = LinkListAdapter(viewModel as LinkButtonClickListener)
        binding.linkRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        binding.linkRecyclerView.adapter = adapter

        viewModel.allLinks.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            val thisActivity = (requireActivity() as HomeActivity)
            makeSnackbar(message, thisActivity)
        })
    }

}
