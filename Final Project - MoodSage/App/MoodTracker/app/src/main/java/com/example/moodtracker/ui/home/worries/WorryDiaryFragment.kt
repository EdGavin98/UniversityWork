package com.example.moodtracker.ui.home.worries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moodtracker.R
import com.example.moodtracker.databinding.FragmentWorryDiaryBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import javax.inject.Inject

class WorryDiaryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: WorryDiaryViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as HomeActivity).homeComponent.inject(this)
        val binding = DataBindingUtil.inflate<FragmentWorryDiaryBinding>(
            inflater,
            R.layout.fragment_worry_diary,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            initRecyclerView(it)
        }
        initObservers()

        return binding.root
    }

    private fun initRecyclerView(binding: FragmentWorryDiaryBinding) {
        val listAdapter = WorryListAdapter(viewModel)
        with(binding.diaryRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initObservers() {
        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            makeSnackbar(message, (requireActivity() as HomeActivity))
        })


        viewModel.clickedWorryDate.observe(viewLifecycleOwner, Observer { dateString ->
            dateString?.let {
                val action = WorryDiaryFragmentDirections.actionWorryDiaryFragmentToWorryDetailsFragment(dateString)
                findNavController().navigate(action)
                viewModel.onItemClickComplete()
            }
        })
    }

}
