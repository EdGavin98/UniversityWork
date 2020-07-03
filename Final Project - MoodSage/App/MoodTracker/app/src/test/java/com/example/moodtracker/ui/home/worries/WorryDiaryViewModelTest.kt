package com.example.moodtracker.ui.home.worries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.data.database.models.entities.Worry
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.worry.WorryRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime


@RunWith(AndroidJUnit4::class)
class WorryDiaryViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var worryRepository: WorryRepository

    lateinit var viewModel : WorryDiaryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(worryRepository)
    }

    @Test
    fun filterUpdateChangesList() {

        val worryList = listOf(
            Worry(
                user = "id",
                date = LocalDateTime.now(),
                severity = 4,
                description = "Test",
                type = "Current"
            ),
            Worry(
                user = "id",
                date = LocalDateTime.now().plusHours(1),
                severity = 8,
                description = "Test",
                type = "Current"
            ),
            Worry(
                user = "id",
                date = LocalDateTime.now().plusHours(2),
                severity = 5,
                description = "Test",
                type = "Hypothetical"
            )
        )

        coEvery { worryRepository.getAllWorries() } returns Resource.success( MutableLiveData(worryList) )


        viewModel.filter.value!!.severity = "1"
        viewModel.filter.value!!.maxSeverity = "10"

        assertThat(viewModel.allWorries.getOrAwaitValue().size, `is`(3))

        viewModel.filter.value!!.severity = "6"

        assertThat(viewModel.allWorries.getOrAwaitValue().size, `is`(1))

        viewModel.filter.value!!.severity = "1"
        viewModel.filter.value!!.type = "Current"
        assertThat(viewModel.allWorries.getOrAwaitValue().size, `is`(2))

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockRepo : WorryRepository) : WorryDiaryViewModel {
        return WorryDiaryViewModel(
            worryRepository = mockRepo
        )
    }
}