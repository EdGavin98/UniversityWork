package com.example.moodtracker.ui.home.worries

import androidx.lifecycle.*
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.worry.WorryRepository
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class WorryDiaryViewModel @Inject constructor(
    private val worryRepository: WorryRepository
) : ViewModel(), WorryItemClickListener {

    val filter = MutableLiveData(ListFilter())

    val allWorries: LiveData<List<Worry>> = Transformations.switchMap(filter) {
        worryRepository.getAllWorries().run {
            println(this.data!!.value)
            when (status) {
                Status.SUCCESS -> {
                    data.map { worries ->
                        worries.filter { matchesFilter(it, getMinDate()) }
                    }
                }
                Status.FAILURE -> {
                    _message.value = message
                    data
                }
            }
        }
    }

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private var _clickedWorryDate = MutableLiveData<String>()
    val clickedWorryDate : LiveData<String>
        get() = _clickedWorryDate

    private fun matchesFilter(worry: Worry, minDate: LocalDateTime): Boolean {
        val settings = filter.value!! //Asserting not null at the start
        val min = settings.severity.toInt()
        val max = settings.maxSeverity.toInt()


        if (settings.type != "All" && worry.type != settings.type) {
            return false
        }

        if (worry.severity < min || worry.severity > max) {
            return false
        }

        if (checkDate(minDate, worry)) {
            return false
        }

        return true
    }

    private fun checkDate(dateFilter: LocalDateTime, worry: Worry): Boolean {
        return worry.date < dateFilter
    }

    private fun getMinDate(): LocalDateTime {
        val currentTime = LocalDateTime.now()
        return when (filter.value!!.date) {
            "Today" -> currentTime.minusDays(1)
            "This Week" -> currentTime.minusWeeks(1)
            "This Month" -> currentTime.minusMonths(1)
            "Past 6 Months" -> currentTime.minusMonths(6)
            "Past Year" -> currentTime.minusYears(1)
            else -> currentTime.minusYears(10)
        }
    }

    inner class ListFilter {
        var date: String = "All"
            set(value) {
                field = value
                filter.value = this
            }

        var severity: String = "1"
            set(value) {
                field = value
                filter.value = this
            }

        var maxSeverity: String = "10"
            set(value) {
                field = value
                filter.value = this
            }

        var type: String = "All"
            set(value) {
                field = value
                filter.value = this
            }
    }

    override fun onItemClick(worryDate: LocalDateTime) {
        _clickedWorryDate.value = worryDate.toString()
    }

    override fun onItemClickComplete() {
        _clickedWorryDate.value = null
    }

}
