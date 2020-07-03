package com.example.moodtracker.ui.home.newmood

import android.content.Context
import androidx.lifecycle.*
import androidx.work.*
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.workers.MoodUploadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject


class NewMoodViewModel @Inject constructor(
    private val moodRepository: MoodRepository,
    private val context: Context
) : ViewModel() {

    private var _pickDate = MutableLiveData<Boolean>()
    val pickDate: LiveData<Boolean>
        get() = _pickDate

    private var _selectedDate = MutableLiveData<LocalDate>()
    val selectedDate: LiveData<LocalDate>
        get() = _selectedDate

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    val canSubmit: LiveData<Boolean> = Transformations.switchMap(_selectedDate) { date ->
        updateCanSubmit(date)
    }

    var commentText = MutableLiveData<String>()

    private var currentMood = 5
    private var isPrivate = false

    init {
        initViewModel()
    }

    fun onPickDate() {
        _pickDate.value = true
    }

    fun onPickDateComplete() {
        _pickDate.value = false
    }

    fun setDate(year: Int, month: Int, day: Int) {
        val date = LocalDate.of(year, month, day)
        _selectedDate.value = date
    }

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            val date = _selectedDate.value!!.atStartOfDay()

            moodRepository.addMood(
                date = date,
                rating = currentMood,
                comment = commentText.value!!,
                isPrivate = isPrivate
            ).apply {
                when (status) {
                    Status.SUCCESS -> {
                        _message.postValue("Mood added")
                        makeWorkRequest(date)
                        withContext(Dispatchers.Main) {
                            initViewModel()
                        }
                    }
                    Status.FAILURE -> {
                        _message.postValue(message)
                    }
                }
            }
        }
    }

    fun onTogglePrivate(checked : Boolean) {
        isPrivate = checked
    }
    fun onMoodChanged(rating: Int) {
        currentMood = rating
    }

    // Private helper functions
    private fun initViewModel() {
        val date = LocalDate.now()
        setDate(date.year, date.monthValue, date.dayOfMonth)
        commentText.value = ""
    }

    private fun makeWorkRequest(date: LocalDateTime) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        OneTimeWorkRequestBuilder<MoodUploadWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf("MOOD_DATE" to date.toString()))
            .build()
            .also {
                WorkManager.getInstance(context).enqueue(it)
            }
    }

    private fun updateCanSubmit(date: LocalDate): LiveData<Boolean> {
        return liveData(viewModelScope.coroutineContext) {
            moodRepository.getMoodByDate(date.atStartOfDay()).run {
                emit(status == Status.FAILURE)
            }
        }
    }

}