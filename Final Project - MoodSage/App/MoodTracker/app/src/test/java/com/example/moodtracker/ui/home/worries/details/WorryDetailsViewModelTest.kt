package com.example.moodtracker.ui.home.worries.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.solution.SolutionRepository
import com.example.moodtracker.repository.worry.WorryRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
class WorryDetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var solutionRepository: SolutionRepository

    @MockK
    lateinit var worryRepository: WorryRepository

    lateinit var viewModel : WorryDetailsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = WorryDetailsViewModel(solutionRepository, worryRepository)
    }

    @Test
    fun getMoodWithSolutionReturnSuccess() {

        coEvery { solutionRepository.getSolutionsForWorry(any()) } returns Resource.success(mockk())

        viewModel.getMoodWithSolution(LocalDateTime.now())

        assertThat(viewModel.worry.getOrAwaitValue(), `is`(notNullValue()))


    }

    @Test
    fun getMoodWithSolutionReturnsFailure() {
        coEvery { solutionRepository.getSolutionsForWorry(any()) } returns Resource.failure("Error", null)

        viewModel.getMoodWithSolution(LocalDateTime.now())

        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))

    }

    @Test
    fun addNavigationEventTest() {
        viewModel.onAddSolutionClicked()

        assertThat(viewModel.addSolutionClicked.getOrAwaitValue(), `is`(true))

        viewModel.onAddSolutionClickedComplete()

        assertThat(viewModel.addSolutionClicked.getOrAwaitValue(), `is`(false))


    }


    @After
    fun tearDown() {
        clearAllMocks()
    }

}