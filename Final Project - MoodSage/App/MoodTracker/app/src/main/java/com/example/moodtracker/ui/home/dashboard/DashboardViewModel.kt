package com.example.moodtracker.ui.home.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.data.sharedprefs.SharedPreferencesManager
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.repository.solution.SolutionRepository
import com.example.moodtracker.repository.worry.WorryRepository
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val accountRepo: AccountRepository,
    private val moodRepo: MoodRepository,
    private val worryRepo: WorryRepository,
    private val solutionRepo: SolutionRepository,
    private val sp: SharedPreferencesManager
) : ViewModel() {

    private val colourMain = sp.getStringPreference("chart_colour_primary")
    private val colourSecondary = sp.getStringPreference("chart_colour_secondary")

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User>
        get() = _currentUser

    private val _moodsByDayData = MutableLiveData<BarData>()
    val moodsByDayData: LiveData<BarData>
        get() = _moodsByDayData

    private val _ratingsOverTimeData = MutableLiveData<LineData>()
    val ratingsOverTime: LiveData<LineData>
        get() = _ratingsOverTimeData

    private val _currentHypotheticalSplitData = MutableLiveData<PieData>()
    val currentHypotheticalSplitData: LiveData<PieData>
        get() = _currentHypotheticalSplitData

    private val _worriesWithSolutionsData = MutableLiveData<PieData>()
    val worriesWithSolutionsData: LiveData<PieData>
        get() = _worriesWithSolutionsData

    private val _averageMoodRating = MutableLiveData<Float>()
    val averageMoodRating: LiveData<Float>
        get() = _averageMoodRating

    private val _moodTargetMessage = MutableLiveData<String>()
    val moodTargetMessage: LiveData<String>
        get() = _moodTargetMessage

    private val _averageWorryRating = MutableLiveData<Float>()
    val averageWorryRating: LiveData<Float>
        get() = _averageWorryRating

    private val _worryTargetMessage = MutableLiveData<String>()
    val worryTargetMessage: LiveData<String>
        get() = _worryTargetMessage


    init {
        setUpDashboard()
    }

    private fun setUpDashboard() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUser()
            getAverageByDayData()
            getWorryAndMoodOverTimeData()
            getCurrentHypotheticalCountData()
            getSolutionData()
            getTargetData()
        }
    }

    private suspend fun getCurrentUser() {
            accountRepo.getCurrentAccount().run {
                when (status) {
                    Status.SUCCESS -> _currentUser.postValue(this.data!!)
                    Status.FAILURE -> _currentUser.postValue(User("Error", "Error", "Error", "Error"))
                }
            }
    }

    private suspend fun getWorryAndMoodOverTimeData() {
        val moodsOverTime = moodRepo.getAllMoodRatings()
        val worriesOverTime = worryRepo.getAllWorrySeverities()
        val data = buildMoodWorryOverTimeDataset(moodsOverTime.data!!, worriesOverTime.data!!, colourMain, colourSecondary)
        _ratingsOverTimeData.postValue(data)
    }

    private suspend fun getAverageByDayData() {
        val moodAverages = moodRepo.getAverageMoodRatingByDay()
        val worryAverages = worryRepo.getWorrySeverityAverageByDay()
        val data = buildMoodWorryByDayRatingDataset(moodAverages.data!!, worryAverages.data!!, colourMain, colourSecondary)
        _moodsByDayData.postValue(data)
    }

    private suspend fun getCurrentHypotheticalCountData() {
        val counts = worryRepo.getCurrentAndHypotheticalCount()
        val data = buildCurrentHypotheticalCountDataset(counts.data!!, colourMain, colourSecondary)
        _currentHypotheticalSplitData.postValue(data)
    }

    private suspend fun getTargetData() {
        moodRepo.getAllMoodRatings().run {
            val average = averageRatingFromThisMonth(data!!)
            getMoodTargetMessage(average)
            _averageMoodRating.postValue(average)
        }

        worryRepo.getAllWorrySeverities().run {
            val average = averageRatingFromThisMonth(data!!)
            getWorryTargetMessage(average)
            _averageWorryRating.postValue(average)
        }
    }

    private fun getMoodTargetMessage(average: Float) {
        val target = currentUser.value?.moodTarget ?: 0
        val distanceToTarget = target - average

        val message = when {
            distanceToTarget <= 0 -> "Congratulations, keep it up!"
            distanceToTarget <= 1 -> "Nearly there!"
            else -> "You can do it!"
        }
        _moodTargetMessage.postValue(message)
    }

    private fun getWorryTargetMessage(average: Float) {
        val target = currentUser.value?.worryTarget ?: 0
        val distanceToTarget = target - average

        val message = when {
            distanceToTarget >= 0 -> "Congratulations, keep it up!"
            distanceToTarget >= 1 -> "Nearly there!"
            else -> "You can do it!"
        }
        _worryTargetMessage.postValue(message)
    }

    private fun averageRatingFromThisMonth(ratings : List<DateRating>): Float {
        val oneMonthAgo = LocalDateTime.now().minusMonths(1)
        val thisMonthRatings = mutableListOf<Int>()
        ratings.forEach {
            if (it.date.isAfter(oneMonthAgo)) {
                thisMonthRatings.add(it.value)
            }
        }
        return thisMonthRatings.average().toFloat()
    }

    private suspend fun getSolutionData() {
        val solutions = solutionRepo.getAllSolutionsForWorries().data!!
        var with = 0
        var without = 0
        for (item in solutions) {
            if (item.solutions.isNullOrEmpty()) {
                without++
            } else {
                with++
            }
        }
        val dataset = buildSolvedWorriesCountDataset(with, without, colourMain, colourSecondary)
        _worriesWithSolutionsData.postValue(dataset)
    }
}