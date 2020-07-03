package com.example.moodtracker.ui.home.worries.solutions


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.moodtracker.databinding.FragmentNewSolutionBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class NewSolutionFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NewSolutionViewModel by viewModels { viewModelFactory }

    private val args: NewSolutionFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as HomeActivity).homeComponent.inject(this)
        viewModel.setDate(LocalDateTime.parse(args.worryDate))
        val binding = FragmentNewSolutionBinding.inflate(inflater, container, false).also {
            it.viewModel = viewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
        initObservers()
        return binding.root
    }

    private fun initObservers() {
        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            if (message.isNotBlank()) {
                makeSnackbar(message, (requireActivity() as HomeActivity))
                viewModel.onMessageDisplayed()
            }
        })
    }


}
