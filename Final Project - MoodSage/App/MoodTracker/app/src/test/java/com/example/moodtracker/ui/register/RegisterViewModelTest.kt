package com.example.moodtracker.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
class RegisterViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var accountRepository: AccountRepository

    lateinit var viewModel : RegisterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(accountRepository)
        resetViewModelData()
    }

    @Test
    fun registerSuccess() {
        coEvery { accountRepository.createAccount(any()) } returns Resource.success(Unit)

        viewModel.onRegister()

        assertThat(viewModel.message.getOrAwaitValue(), containsString("success"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(true))

    }

    @Test
    fun registerIncorrectDetails() {

        viewModel.forename.value = ""
        viewModel.onRegister()
        assertThat(viewModel.message.getOrAwaitValue(), containsString("name"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(false))

        resetViewModelData()

        viewModel.email.value = "email_email.com"
        viewModel.onRegister()
        assertThat(viewModel.message.getOrAwaitValue(), containsString("email"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(false))

        resetViewModelData()


        viewModel.email.value = "email_email.com"
        viewModel.onRegister()
        assertThat(viewModel.message.getOrAwaitValue(), containsString("email"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(false))

        resetViewModelData()

        viewModel.password.value = ""
        viewModel.onRegister()
        assertThat(viewModel.message.getOrAwaitValue(), containsString("password"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(false))

        resetViewModelData()

        viewModel.passwordConfirm.value = "nonmatching"
        viewModel.onRegister()
        assertThat(viewModel.message.getOrAwaitValue(), containsString("Passwords"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(false))

    }

    @Test
    fun registerError() {
        coEvery { accountRepository.createAccount(any()) } returns Resource.failure("Error", null)

        viewModel.onRegister()

        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
        assertThat(viewModel.registrationSuccessful.getOrAwaitValue(), `is`(false))

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    private fun createViewModel(mockRepo : AccountRepository) : RegisterViewModel{
        return RegisterViewModel(
            repo = mockRepo
        )
    }

    private fun resetViewModelData() {
        viewModel.email.value = "test@test.com"
        viewModel.password.value = "testpass"
        viewModel.passwordConfirm.value = "testpass"
        viewModel.forename.value = "TestName"
        viewModel.surname.value = "TestSurname"
    }
}