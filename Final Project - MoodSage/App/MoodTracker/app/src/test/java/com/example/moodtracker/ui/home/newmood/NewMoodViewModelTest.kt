package com.example.moodtracker.ui.home.newmood

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.startsWith
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewMoodViewModelTest {

    @get:Rule
    val rule : TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var  moodRepository : MoodRepository
    lateinit var viewModel : NewMoodViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(moodRepository)
    }

    @Test
    fun createNewMoodReturnsSuccess() {

        coEvery { moodRepository.addMood(any(), any(), any(), any()) } returns Resource.success(Unit)
        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), startsWith("Mood added"))

    }

    @Test
    fun createNewMoodReturnsFailure() {

        coEvery { moodRepository.addMood(any(), any(), any(), any()) } returns Resource.failure("Error", null)

        viewModel.commentText.value = "Test"
        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
        assertThat(viewModel.commentText.getOrAwaitValue(), `is`("Test"))

    }

    @Test
    fun dateSelectedAlreadyHasMood() {

        coEvery { moodRepository.getMoodByDate(any()) } returns Resource.success(mockk())

        viewModel.setDate(1, 1, 1)

        assertThat(viewModel.canSubmit.getOrAwaitValue(), `is`(false))

    }

    @Test
    fun dateSelectedHasNoMood() {

        coEvery { moodRepository.getMoodByDate(any()) } returns Resource.failure("No Date", null)
        viewModel.setDate(1, 1, 1)

        assertThat(viewModel.canSubmit.getOrAwaitValue(), `is`(true))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    private fun createViewModel(mockRepository: MoodRepository): NewMoodViewModel {
        return NewMoodViewModel(
            moodRepository = mockRepository,
            context = ApplicationProvider.getApplicationContext()
        )
    }
}