package com.example.moodtracker.ui.home.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var accountRepository: AccountRepository

    lateinit var viewModel : SettingsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(accountRepository)
    }

    @Test
    fun deleteAccountSuccess() {
        coEvery { accountRepository.deleteAccount() } returns Resource.success(Unit)
        coEvery { accountRepository.logout() } returns Resource.success(Unit)

        viewModel.deleteAccount()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("success"))
        assertThat(viewModel.logOutComplete.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun deleteAccountWithError() {
        coEvery { accountRepository.deleteAccount() } returns Resource.failure("Error", null)

        viewModel.deleteAccount()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("Error"))
    }

    @Test
    fun logOutSuccess() {
        coEvery { accountRepository.logout() } returns Resource.success(Unit)

        viewModel.logout()

        assertThat(viewModel.logOutComplete.getOrAwaitValue(), `is`(true))
    }
    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockRepo: AccountRepository) : SettingsViewModel {
        return SettingsViewModel(
            accountRepo = mockRepo,
            context = ApplicationProvider.getApplicationContext()
        )
    }

}