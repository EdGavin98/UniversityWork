package com.example.moodtracker.ui.home.newthought

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moodtracker.R
import com.example.moodtracker.databinding.FragmentNewThoughtBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class NewThoughtFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NewThoughtViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as HomeActivity).homeComponent.inject(this)
        val binding = DataBindingUtil.inflate<FragmentNewThoughtBinding>(
            inflater,
            R.layout.fragment_new_thought,
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
        viewModel.showDateTimePicker.observe(viewLifecycleOwner, Observer { show ->
            if (show) {
                showDateTimePickerDialog()
                viewModel.onShowDateTimePickerComplete()
            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            val thisActivity = (requireActivity() as HomeActivity)
            makeSnackbar(message, thisActivity)
        })
    }


    private fun showDateTimePickerDialog() {
        val currentDateTime = LocalDateTime.now()
        val startYear = currentDateTime.year
        val startMonth = currentDateTime.monthValue
        val startDay = currentDateTime.dayOfMonth
        val startHour = currentDateTime.hour
        val startMinute = currentDateTime.minute

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        viewModel.setDateTime(year, month + 1, day, hour, minute)
                    },
                    startHour,
                    startMinute,
                    true
                ).show()
            },
            startYear,
            startMonth - 1,
            startDay
        ).show()
    }

}
