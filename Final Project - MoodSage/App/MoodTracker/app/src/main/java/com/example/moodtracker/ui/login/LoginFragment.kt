package com.example.moodtracker.ui.login


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moodtracker.R
import com.example.moodtracker.databinding.FragmentLoginBinding
import com.example.moodtracker.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as LoginActivity).loginComponent.inject(this)
        val binding = FragmentLoginBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            initObservers(it)
        }
        return binding.root
    }

    private fun initObservers(binding: FragmentLoginBinding) {
        viewModel.registerPressed.observe(viewLifecycleOwner, Observer {
            if (it) startActivity(Intent(requireContext(), RegisterActivity::class.java)).also {
                viewModel.onRegisterPressedComplete()
            }
        })

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_loadingFragment)
                viewModel.onLoginComplete()
            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(binding.loginCoordinator, message, Snackbar.LENGTH_SHORT).show()
        })
    }

}
