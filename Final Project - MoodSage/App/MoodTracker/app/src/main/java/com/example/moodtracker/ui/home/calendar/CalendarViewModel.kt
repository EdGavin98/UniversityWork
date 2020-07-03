package com.example.moodtracker.ui.home.calendar

import androidx.lifecycle.*
import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.mood.MoodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class CalendarViewModel @Inject constructor(private val moodRepository: MoodRepository) :
    ViewModel() {


    private var _moods = MutableLiveData<List<Mood>>()
    val moods: LiveData<List<Mood>>
        get() = _moods

    private var _selectedMood = MutableLiveData<Mood>()
    val selectedMood: LiveData<Mood>
        get() = _selectedMood

    private var _moodExists = MutableLiveData<Boolean>()
    val moodExists: LiveData<Boolean>
        get() = _moodExists

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private var _deleteClicked = MutableLiveData<Boolean>()
    val deleteClicked : LiveData<Boolean>
        get() =_deleteClicked


    val monthAverage: LiveData<Double> = Transformations.switchMap(_moods) { moods ->
        liveData<Double> { emit(getAverageRating(moods)) }
    }

    init {
        val thisMonth = LocalDate.now()
        onMonthChanged(LocalDate.of(thisMonth.year, thisMonth.monthValue, 1))
        _selectedMood.postValue(
            Mood(
                userId = "",
                rating = 0,
                date = LocalDateTime.now(),
                comment = ""
            )
        )
    }


    fun onMonthChanged(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            moodRepository.getMoodsFromMonth(date).run {
                when (status) {
                    Status.SUCCESS -> {
                        _moods.postValue(data!!)
                    }
                    Status.FAILURE -> {
                        _moods.postValue(emptyList())
                        _message.postValue(message)
                    }
                }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            moodRepository.getMoodByDate(date.atStartOfDay()).run {
                when (status) {
                    Status.SUCCESS -> {
                        _moodExists.postValue(true)
                        _selectedMood.postValue(data!!)
                    }
                    Status.FAILURE -> {
                        _moodExists.postValue(false)
                        _message.postValue(message)
                    }
                }
            }
        }
    }

    fun onDeleteClicked() {
        _deleteClicked.value = true
    }

    fun onDeleteClickedComplete() {
        _deleteClicked.value = false
    }

    fun deleteMood() {
        viewModelScope.launch(Dispatchers.IO) {
            moodRepository.deleteMood(_selectedMood.value!!.date).run {
                when (status) {
                    Status.SUCCESS -> {
                        _moodExists.postValue(false)
                        _message.postValue("Mood deleted successfully")
                    }
                    Status.FAILURE -> {
                        _message.postValue(message)
                    }
                }
            }
        }
    }

    private fun getAverageRating(moods: List<Mood>): Double {
        return if (moods.isEmpty()) {
            0.0
        } else {
            var total = 0
            for (mood in moods) {
                total += mood.rating
            }
            total.toDouble() / moods.size
        }

    }

}