package com.example.moodtracker.ui.home.newthought

import android.content.Context
import androidx.lifecycle.*
import androidx.work.*
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.worry.WorryRepository
import com.example.moodtracker.workers.WorryUploadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class NewThoughtViewModel @Inject constructor(
    private val worryRepository: WorryRepository,
    private val context: Context
) : ViewModel() {

    // Value properties
    private var _currentDateTime = MutableLiveData<LocalDateTime>()
    val currentDateTime: LiveData<LocalDateTime>
        get() = _currentDateTime

    private var _worryType = MutableLiveData<String>("Current")
    val worryType: LiveData<String>
        get() = _worryType

    val canSubmit: LiveData<Boolean> = Transformations.switchMap(_currentDateTime) { date ->
        updateCanSubmit(date)
    }

    var worryDescription = MutableLiveData<String>("")

    var severity = MutableLiveData<Int>(0)

    private var private = false

    // Event properties
    private var _showDateTimePicker = MutableLiveData<Boolean>(false)
    val showDateTimePicker: LiveData<Boolean>
        get() = _showDateTimePicker

    private var _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private var _showCurrentHypotheticalDescription = MutableLiveData<Boolean>(false)
    val showCurrentHypotheticalDescription: LiveData<Boolean>
        get() = _showCurrentHypotheticalDescription

    init {
        _currentDateTime.value = LocalDateTime.now()
    }

    fun onTypeSelected(type: Int) {
        _worryType.value = if (type == 0) "Current" else "Hypothetical"
    }

    fun onTogglePrivate(checked: Boolean) {
        private = checked
    }

    fun onSubmit() {
        viewModelScope.launch(Dispatchers.IO) {
            val date = _currentDateTime.value!!
            worryRepository.addNewWorry(
                date = date,
                description = worryDescription.value!!,
                severity = severity.value!! + 1, //Using selected item position rather than values, so add 1 to get the actual value
                type = worryType.value!!,
                private = private
            ).also {
                when (it.status) {
                    Status.SUCCESS -> {
                        _message.postValue("Worry added successfully")
                        makeWorkRequest(date)
                        withContext(Dispatchers.Main) {
                            resetViewModel()
                        }
                    }

                    Status.FAILURE -> {
                        _message.postValue(it.message)
                    }
                }
            }
        }
    }

    fun onShowDateTimePicker() {
        _showDateTimePicker.value = true
    }

    fun onShowDateTimePickerComplete() {
        _showDateTimePicker.value = false
    }

    fun setDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        _currentDateTime.value = LocalDateTime.of(year, month, day, hour, minute)
    }

    fun onToggleShowDescription() {
        _showCurrentHypotheticalDescription.value = !(_showCurrentHypotheticalDescription.value!!)
    }


    ////////////////////////// Helpers ///////////////////////////////

    private fun resetViewModel() {
        _currentDateTime.value = LocalDateTime.now()
        severity.value = 0
        worryDescription.value = ""
    }

    private fun makeWorkRequest(date: LocalDateTime) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        OneTimeWorkRequestBuilder<WorryUploadWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf("WORRY_DATE" to date.toString()))
            .build()
            .also {
                WorkManager.getInstance(context).enqueue(it)
            }
    }

    private fun updateCanSubmit(date: LocalDateTime): LiveData<Boolean> {
        return liveData(viewModelScope.coroutineContext) {
            worryRepository.getWorryByDate(date).run {
                emit(status == Status.FAILURE)
            }
        }
    }

}