package com.example.moodtracker.ui.home.settings.links

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moodtracker.data.network.models.LinkDto
import com.example.moodtracker.data.network.models.UserDto
import com.example.moodtracker.repository.Resource
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.ui.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LinkViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var accountRepository: AccountRepository

    lateinit var viewModel : LinkViewModel


    @Before
    fun setUp() {
        val linkList = listOf(
            LinkDto (
                status = 0,
                user = UserDto(
                    id = "1",
                    forename = "Test",
                    surname = "Test",
                    email = "Email"
                )
            )
        )
        MockKAnnotations.init(this)
        coEvery { accountRepository.getLinks() } returns Resource.success(linkList)

        viewModel = createViewModel(accountRepository)
    }

    @Test
    fun initializesCorrectly() {
        assertThat(viewModel.allLinks.getOrAwaitValue(), `is`(notNullValue()))
        assertThat(viewModel.allLinks.getOrAwaitValue().size, `is`(1))
    }

    @Test
    fun acceptLinkSuccess() {

        coEvery { accountRepository.acceptLink(any()) } returns Resource.success(Unit)

        viewModel.allLinks.getOrAwaitValue().let { result ->
            assertThat(result[0].status, `is`(0))
        }

        viewModel.onAcceptClicked("id")
        coVerify(atLeast = 2, atMost = 2) {
            accountRepository.getLinks()
        }

    }

    @Test
    fun acceptLinkError() {

        coEvery { accountRepository.acceptLink(any()) } returns Resource.failure("Error", null)

        viewModel.allLinks.getOrAwaitValue().let { result ->
            assertThat(result[0].status, `is`(0))
        }

        viewModel.onAcceptClicked("id")

        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
        coVerify(atLeast = 1, atMost = 1) {
            accountRepository.getLinks()
        }
    }

    @Test
    fun removeLinkSuccess() {

        coEvery { accountRepository.removeLink(any()) } returns Resource.success(Unit)

        viewModel.allLinks.getOrAwaitValue().let { result ->
            assertThat(result[0], `is`(notNullValue()))
        }

        viewModel.onRemoveClicked("id")

        coVerify(atLeast = 2, atMost = 2) {
            accountRepository.getLinks()
        }
    }

    @Test
    fun removeLinkError() {

        coEvery { accountRepository.removeLink(any()) } returns Resource.failure("Error", null)

        viewModel.allLinks.getOrAwaitValue().let { result ->
            assertThat(result[0], `is`(notNullValue()))
        }

        viewModel.onRemoveClicked("id")
        assertThat(viewModel.message.getOrAwaitValue(), `is`("Error"))
        coVerify(atLeast = 1, atMost = 1) {
            accountRepository.getLinks()
        }


    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel(mockRepo: AccountRepository) : LinkViewModel {
        return LinkViewModel(
            accountRepo = mockRepo
        )
    }
}