package com.example.moodtracker.ui.home.settings.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodtracker.databinding.FragmentProfileBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import javax.inject.Inject


class ProfileFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ProfileViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as HomeActivity).homeComponent.inject(this)
        val binding = FragmentProfileBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            if (message.isNotBlank()) {
                makeSnackbar(message, (requireActivity() as HomeActivity))
                viewModel.onMessageDisplayed()
            }
        })

        return binding.root
    }


}
