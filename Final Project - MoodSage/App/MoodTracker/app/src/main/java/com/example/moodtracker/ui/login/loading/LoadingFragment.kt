package com.example.moodtracker.ui.login.loading

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moodtracker.R
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.login.LoginActivity
import javax.inject.Inject

class LoadingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoadingViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as LoginActivity).loginComponent.inject(this)
        initObservers()
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    private fun initObservers() {
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                LoadingViewModel.LoadingState.FINISHED -> Intent(
                    requireContext(),
                    HomeActivity::class.java
                ).also {
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(it)
                }

                LoadingViewModel.LoadingState.LOADING -> {
                }

                LoadingViewModel.LoadingState.ERROR -> {
                    Toast.makeText(requireContext(), "Error loading data", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                null -> {
                }
            }

        })
    }

}
