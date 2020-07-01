package com.example.moodtracker.ui.home.calendar


import android.app.AlertDialog
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
import com.example.moodtracker.databinding.FragmentCalendarBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import javax.inject.Inject


class CalendarFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CalendarViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as HomeActivity).homeComponent.inject(this)
        val binding = DataBindingUtil.inflate<FragmentCalendarBinding>(
            inflater,
            R.layout.fragment_calendar,
            container,
            false
        ).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            initObservers(it)
        }

        return binding.root
    }

    private fun initObservers(binding: FragmentCalendarBinding) {
        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            makeSnackbar(message, (requireActivity() as HomeActivity))
        })

        binding.materialCalendar.setOnDateChangedListener { _, day, selected ->
            if (selected) viewModel.onDateSelected(day.date)
        }

        binding.materialCalendar.setOnMonthChangedListener { _, day ->
            viewModel.onMonthChanged(day.date)
        }

        viewModel.deleteClicked.observe(viewLifecycleOwner, Observer { clicked ->
            if (clicked) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Are you sure?")
                    setMessage("This action cannot be undone\nRequires a network connection")
                    setPositiveButton("Yes") { dialog, id ->
                        viewModel.deleteMood()
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

    }


}
