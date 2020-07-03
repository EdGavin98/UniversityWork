package com.example.moodtracker.ui.home.worries.solutions

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.solution.SolutionRepository
import com.example.moodtracker.workers.SolutionUploadWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class NewSolutionViewModel @Inject constructor(
    private val solutionRepo: SolutionRepository,
    private val context: Context
): ViewModel() {

    var description = MutableLiveData<String>("")
    var advantages = MutableLiveData<String>("")
    var disadvantages = MutableLiveData<String>("")

    private var worryDate = LocalDateTime.now()

    private val _message = MutableLiveData<String>("")
    val message : LiveData<String>
        get()= _message

    fun setDate(date : LocalDateTime) {
        worryDate = date
    }

    fun onSubmit() {
        if (isValid()) {
            addSolution()
        }
    }

    fun onMessageDisplayed() {
        _message.value = ""
    }


    private fun isValid(): Boolean {
        if (description.value.isNullOrBlank() || description.value!!.length >= 500) {
            _message.value = "Description field is invalid"
            return false
        }

        if (advantages.value.isNullOrBlank() || advantages.value!!.length >= 500) {
            _message.value = "Advantages field is invalid"
            return false
        }

        if (disadvantages.value.isNullOrBlank() || disadvantages.value!!.length >= 500) {
            _message.value = "Disadvantages field is invalid"
            return false
        }

        return true
    }

    private fun addSolution() {
        viewModelScope.launch {
            solutionRepo.addSolution(
                description = description.value!!,
                advantages = advantages.value!!,
                disadvantages = disadvantages.value!!,
                worryDate = worryDate
            ).also { result ->
                withContext(Dispatchers.Main) {handleResult(result)}
            }
        }
    }

    private fun handleResult(result: Resource<Long>) {
        when (result.status) {
            Status.SUCCESS -> {
                _message.postValue("Solution added successfully")
                makeWorkRequest(result.data!!)
                resetViewModel()
            }
            Status.FAILURE -> {
                _message.postValue(result.message)
            }
        }
    }

    private fun resetViewModel() {
        description.value = ""
        advantages.value = ""
        disadvantages.value = ""
    }

    private fun makeWorkRequest(id : Long) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        OneTimeWorkRequestBuilder<SolutionUploadWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf("SOLUTION_ID" to id))
            .build()
            .also {
                WorkManager.getInstance(context).enqueue(it)
            }
    }

}