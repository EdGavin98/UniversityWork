package com.example.moodtracker.ui.home.worries.details


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.moodtracker.databinding.FragmentWorryDetailsBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import org.threeten.bp.LocalDateTime
import javax.inject.Inject


class WorryDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: WorryDetailsViewModel by viewModels { viewModelFactory }

    private val args: WorryDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as HomeActivity).homeComponent.inject(this)
        LocalDateTime.parse(args.worryDate).also { viewModel.getMoodWithSolution(it)}
        val binding = FragmentWorryDetailsBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
            initRecyclerView(it)
        }
        initObservers()

        return binding.root
    }

    private fun initRecyclerView(binding: FragmentWorryDetailsBinding) {
        with(binding.detailsSolutionsRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = SolutionListAdapter()
            PagerSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun initObservers() {
        viewModel.addSolutionClicked.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                val action = WorryDetailsFragmentDirections.actionWorryDetailsFragmentToNewSolutionFragment(args.worryDate)
                findNavController().navigate(action)
                viewModel.onAddSolutionClickedComplete()
            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            makeSnackbar(message, (requireActivity() as HomeActivity))
        })

        viewModel.deleteClicked.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Are you sure?")
                    setMessage("This action cannot be undone\nRequires a network connection")
                    setPositiveButton("Yes") { dialog, id ->
                        viewModel.deleteWorry()
                        viewModel.onDeleteClickedComplete()
                    }
                    setNegativeButton("No") { dialog, id ->
                        dialog.cancel()
                        viewModel.onDeleteClickedComplete()
                    }
                    show()
                }
            }
        })

        viewModel.deleteComplete.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                findNavController().popBackStack()
            }
        })
    }


}
