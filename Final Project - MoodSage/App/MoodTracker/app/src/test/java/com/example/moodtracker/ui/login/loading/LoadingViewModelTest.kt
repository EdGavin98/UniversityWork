package com.example.moodtracker.ui.login.loading

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.mood.MoodRepository
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

@RunWith(AndroidJUnit4::class)
class LoadingViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var  moodRepository : MoodRepository
    @MockK
    lateinit var worryRepository : WorryRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        //Most code from this viewmodel is in the init block, so need to initialise during test instead
    }

    @Test
    fun worryFailSetsStateToError() {
        coEvery { moodRepository.getLatestMoods() } returns Resource.success(Unit)
        coEvery { worryRepository.getLatestWorries() } returns Resource.failure("Error", null)

        val viewModel = createViewModel(moodRepository, worryRepository)
        Thread.sleep(200) //Wait for init to finish
        assertThat(viewModel.loadingStatus.getOrAwaitValue(), `is`(LoadingViewModel.LoadingState.ERROR))
    }

    @Test
    fun moodFailSetsStateToError() {
        coEvery { moodRepository.getLatestMoods() } returns Resource.failure("Error", null)
        coEvery { worryRepository.getLatestWorries() } returns Resource.success(Unit)

        val viewModel = createViewModel(moodRepository, worryRepository)

        Thread.sleep(200) //Wait for init to finish
        assertThat(viewModel.loadingStatus.getOrAwaitValue(), `is`(LoadingViewModel.LoadingState.ERROR))
    }

    @Test
    fun bothSuccessSetsStateToFinish() {
        coEvery { moodRepository.getLatestMoods() } returns Resource.success(Unit)
        coEvery { worryRepository.getLatestWorries() } returns Resource.success(Unit)

        val viewModel = createViewModel(moodRepository, worryRepository)
        Thread.sleep(200) //Wait for init to finish

        assertThat(viewModel.loadingStatus.getOrAwaitValue(), `is`(LoadingViewModel.LoadingState.FINISHED))
    }


    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockMoodRepo: MoodRepository, mockWorryRepo: WorryRepository): LoadingViewModel {
        return LoadingViewModel(
            moodRepo = mockMoodRepo,
            worryRepo = mockWorryRepo
        )
    }
}