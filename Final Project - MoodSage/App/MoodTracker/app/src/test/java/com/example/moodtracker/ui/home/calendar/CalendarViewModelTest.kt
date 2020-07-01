package com.example.moodtracker.ui.home.calendar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.mood.MoodRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@RunWith(AndroidJUnit4::class)
class CalendarViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var  moodRepository : MoodRepository
    lateinit var viewModel : CalendarViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { moodRepository.getMoodsFromMonth(eq(LocalDate.of(2020, 4, 1))) } returns Resource.success(listOf(Mood(LocalDateTime.now(), "", 1, "")))
        coEvery { moodRepository.getMoodsFromMonth(eq(LocalDate.of(2020, 5, 1))) } returns Resource.failure("No moods", null)

        viewModel = createViewModel(moodRepository)
    }

    @Test
    fun selectedMonthHasMoods() {

        viewModel.onMonthChanged(LocalDate.of(2020, 4, 1))

        assertThat(viewModel.moods.getOrAwaitValue(), `is`(notNullValue()))
    }

    @Test
    fun selectedMonthHasNoMoods() {

        viewModel.onMonthChanged(LocalDate.of(2020, 5, 1))

        assertThat(viewModel.message.getOrAwaitValue(), `is`("No moods"))
        assertThat(viewModel.moods.getOrAwaitValue(), `is`(emptyList()))
    }

    @Test
    fun selectedDayHasMood() {
        coEvery { moodRepository.getMoodByDate(any()) } returns Resource.success(Mood(LocalDateTime.now(), "", 1, ""))

        viewModel.onDateSelected(LocalDate.now())

        assertThat(viewModel.moodExists.getOrAwaitValue(), `is`(true))
        assertThat(viewModel.selectedMood.getOrAwaitValue(), `is`(notNullValue()))
    }

    @Test
    fun selectedDayHasNoMood() {
        coEvery { moodRepository.getMoodByDate(any()) } returns Resource.failure("No Mood", null)

        viewModel.onDateSelected(LocalDate.now())

        assertThat(viewModel.moodExists.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.message.getOrAwaitValue(), `is`("No Mood"))
    }

    @After
    fun finalTearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockRepo : MoodRepository): CalendarViewModel {
        return CalendarViewModel(
            moodRepository = mockRepo
        )
    }
}