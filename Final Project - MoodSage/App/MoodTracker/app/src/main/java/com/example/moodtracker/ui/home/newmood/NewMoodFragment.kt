package com.example.moodtracker.ui.home.newmood


import android.app.DatePickerDialog
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
import com.example.moodtracker.databinding.FragmentNewMoodBinding
import com.example.moodtracker.ui.home.HomeActivity
import com.example.moodtracker.ui.home.makeSnackbar
import org.threeten.bp.LocalDate
import javax.inject.Inject

class NewMoodFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: NewMoodViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity!! as HomeActivity).homeComponent.inject(this)
        val binding = DataBindingUtil.inflate<FragmentNewMoodBinding>(
            inflater,
            R.layout.fragment_new_mood,
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
        viewModel.pickDate.observe(this, Observer { clicked ->
            if (clicked) openCalendar()
        })
        viewModel.message.observe(viewLifecycleOwner, Observer { message ->
            makeSnackbar(message, (requireActivity() as HomeActivity))
        })

    }

    private fun openCalendar() {
        val date = LocalDate.now()
        DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                viewModel.setDate(selectedYear, selectedMonth + 1, selectedDay)
            },
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        ).show()

        viewModel.onPickDateComplete()
    }


}
