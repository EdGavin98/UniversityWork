package com.example.moodtracker.ui.home.worries.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.database.models.returnobjects.WorryWithSolutions
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.solution.SolutionRepository
import com.example.moodtracker.repository.worry.WorryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class WorryDetailsViewModel @Inject constructor(
    private val solutionRepo : SolutionRepository,
    private val worryRepository: WorryRepository
) : ViewModel() {


    private val _worry = MutableLiveData<WorryWithSolutions>()
    val worry : LiveData<WorryWithSolutions>
        get() = _worry

    private val _addSolutionClicked = MutableLiveData<Boolean>()
    val addSolutionClicked : LiveData<Boolean>
        get() = _addSolutionClicked

    private val _deleteClicked = MutableLiveData<Boolean>()
    val deleteClicked : LiveData<Boolean>
        get()= _deleteClicked

    private val _deleteComplete = MutableLiveData<Boolean>()
    val deleteComplete : LiveData<Boolean>
        get()= _deleteComplete

    private val _message = MutableLiveData<String>()
    val message : LiveData<String>
        get() = _message

    fun getMoodWithSolution(date: LocalDateTime) {
        viewModelScope.launch(Dispatchers.IO)  {
            solutionRepo.getSolutionsForWorry(date).run {
                when (status) {
                    Status.SUCCESS -> {
                        _worry.postValue(data)
                    }
                    Status.FAILURE -> {
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

    fun deleteWorry() {
        viewModelScope.launch(Dispatchers.IO) {
            worryRepository.deleteWorry(_worry.value!!.worry!!.date).run {
                when (status) {
                    Status.SUCCESS -> _deleteComplete.postValue(true)
                    Status.FAILURE -> _message.postValue(message)
                }
            }
        }
    }

    fun onAddSolutionClicked() {
        _addSolutionClicked.value = true
    }

    fun onAddSolutionClickedComplete() {
        _addSolutionClicked.value = false
    }

}