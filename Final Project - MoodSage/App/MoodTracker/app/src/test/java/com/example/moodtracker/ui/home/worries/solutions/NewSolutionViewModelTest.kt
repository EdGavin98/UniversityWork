package com.example.moodtracker.ui.home.worries.solutions

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.solution.SolutionRepository
import com.example.moodtracker.ui.getOrAwaitValue
import com.example.moodtracker.ui.home.worries.details.WorryDetailsViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(AndroidJUnit4::class)
class NewSolutionViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var solutionRepository: SolutionRepository

    lateinit var viewModel : NewSolutionViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = NewSolutionViewModel(
            solutionRepository,
            ApplicationProvider.getApplicationContext()
        )

    }

    @Test
    fun submitWithInvalidDetails() {

        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("Description"))

        viewModel.description.value = "DescriptionTest"
        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("Advantages"))

        viewModel.advantages.value = "AdvantagesTest"
        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("Disadvantages"))

    }

    @Test
    fun submitWithValidDetails() {

        viewModel.advantages.value = "AdvantagesTest"
        viewModel.description.value = "DescriptionTest"
        viewModel.disadvantages.value = "DisadvantagesTest"

        coEvery { solutionRepository.addSolution(any(), any(), any(), any()) } returns Resource.success(1L)

        viewModel.onSubmit()
        assertThat(viewModel.message.getOrAwaitValue(), containsString("success"))
        assertThat(viewModel.description.getOrAwaitValue(), `is`(""))
        assertThat(viewModel.advantages.getOrAwaitValue(), `is`(""))
        assertThat(viewModel.disadvantages.getOrAwaitValue(), `is`(""))

    }

    @Test
    fun submitReturnsFailure() {
        viewModel.advantages.value = "AdvantagesTest"
        viewModel.description.value = "DescriptionTest"
        viewModel.disadvantages.value = "DisadvantagesTest"

        coEvery { solutionRepository.addSolution(any(), any(), any(), any()) } returns Resource.failure("Error", null)

        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
        assertThat(viewModel.disadvantages.getOrAwaitValue(), `is`("DisadvantagesTest"))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}