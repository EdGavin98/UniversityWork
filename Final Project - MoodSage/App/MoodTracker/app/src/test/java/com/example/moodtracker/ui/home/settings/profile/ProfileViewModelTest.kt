package com.example.moodtracker.ui.home.settings.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var accountRepository: AccountRepository

    lateinit var viewModel : ProfileViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { accountRepository.getCurrentAccount() } returns Resource.success(
            User (
                id = "id",
                forename = "Test",
                surname = "update",
                email = "test@update.com"
            )
        )
        viewModel = createViewModel(accountRepository)
    }

    @Test
    fun initialiseWorks() {
        assertThat(viewModel.currentUser.getOrAwaitValue(), `is`(notNullValue()))
    }

    @Test
    fun updateDetailsReturnsError() {
        coEvery { accountRepository.updateAccount(any()) } returns Resource.failure("Error", null)

        viewModel.onSubmitChanges()

        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
    }

    @Test
    fun updateDetailsReturnsSuccess() {
        coEvery { accountRepository.updateAccount(any()) } returns Resource.success(mockk())

        viewModel.onSubmitChanges()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("success"))
    }

    @Test
    fun updateDetailsFailsValidation() {
        viewModel.currentUser.value!!.email = "email"
        viewModel.onSubmitChanges()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("invalid"))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockRepo : AccountRepository): ProfileViewModel {
        return ProfileViewModel(
            accountRepo = mockRepo
        )
    }
}