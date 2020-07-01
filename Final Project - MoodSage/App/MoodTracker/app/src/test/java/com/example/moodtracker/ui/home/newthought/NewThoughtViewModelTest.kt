package com.example.moodtracker.ui.home.newthought

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.worry.WorryRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.startsWith
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
class NewThoughtViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var worryRepository: WorryRepository

    lateinit var viewModel : NewThoughtViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(worryRepository)
    }

    @Test
    fun submitMoodReturnsSuccess() {
        coEvery { worryRepository.addNewWorry(any(), any(), any(), any()) } returns Resource.success(Unit)

        viewModel.onSubmit()

        assertThat(viewModel.message.getOrAwaitValue(), startsWith("Worry added"))
        assertThat(viewModel.worryDescription.getOrAwaitValue(), `is`(""))

    }

    @Test
    fun submitMoodReturnsFailure()  {
        coEvery { worryRepository.addNewWorry(any(), any(), any(), any()) } returns Resource.failure("Error", null)

        viewModel.worryDescription.value = "Testing no clear"
        viewModel.onSubmit()
        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
        assertThat(viewModel.worryDescription.getOrAwaitValue(), `is`("Testing no clear"))
    }

    @Test
    fun dateSelectedAlreadyHasMood() {
        coEvery { worryRepository.getWorryByDate(any())} returns Resource.success(mockk())

        viewModel.setDateTime(0, 1, 1, 0, 0)
        assertThat(viewModel.canSubmit.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun dateSelectedHasNoMood() = runBlocking{
        coEvery { worryRepository.getWorryByDate(any()) } returns Resource.failure("No Mood", null)
        viewModel = createViewModel(worryRepository)

        viewModel.setDateTime(0, 1, 1, 0, 0)

        assertThat(viewModel.currentDateTime.getOrAwaitValue(), `is`(LocalDateTime.of(0, 1, 1, 0, 0)))
        assertThat(viewModel.canSubmit.getOrAwaitValue(), `is`(true))

    }

    @Test
    fun worryTypeUpdatesCorrectly() {
        viewModel.onTypeSelected(0)
        assertThat(viewModel.worryType.getOrAwaitValue(), `is`("Current"))
        viewModel.onTypeSelected(1)
        assertThat(viewModel.worryType.getOrAwaitValue(), `is`("Hypothetical"))
    }


    @After
    fun tearDown() {
        clearAllMocks()
    }



    private fun createViewModel(mockRepo : WorryRepository): NewThoughtViewModel {
        InstrumentationRegistry.getInstrumentation().context.let { context ->
            return NewThoughtViewModel(
                context = ApplicationProvider.getApplicationContext(),
                worryRepository = mockRepo
            )
        }
    }
}