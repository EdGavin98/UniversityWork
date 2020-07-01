package com.example.moodtracker.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var  accountRepository : AccountRepository

    lateinit var viewModel : LoginViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = createViewModel(accountRepository)
    }

    @Test
    fun loginSuccess() {

        coEvery { accountRepository.login(any(), any()) } returns Resource.success(Unit)

        viewModel.onLoginPressed()

        assertThat(viewModel.loginStatus.getOrAwaitValue(), `is`(true))

    }

    @Test
    fun loginFailure() {

        coEvery { accountRepository.login(any(), any()) } returns Resource.failure("Error", null)

        viewModel.onLoginPressed()

        assertThat(viewModel.loginStatus.getOrAwaitValue(), `is`(false))
        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
    }

    @Test
    fun registerNavigation() {
        viewModel.onRegisterPressed()
        assertThat(viewModel.registerPressed.getOrAwaitValue(), `is`(true))
        viewModel.onRegisterPressedComplete()
        assertThat(viewModel.registerPressed.getOrAwaitValue(), `is`(false))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockRepo: AccountRepository): LoginViewModel {
        return LoginViewModel(
            accountRepo = mockRepo
        )
    }
}