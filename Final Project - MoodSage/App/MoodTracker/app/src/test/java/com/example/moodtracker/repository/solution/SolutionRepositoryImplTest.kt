package com.example.moodtracker.repository.solution

import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.repository.account.AccountRepositoryImpl
import com.example.moodtracker.repository.data.sharedprefs.MockSharedPrefs
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.threeten.bp.LocalDateTime

class SolutionRepositoryImplTest {

    private lateinit var solutionRepo: SolutionRepositoryImpl
    private lateinit var accountRepo: AccountRepositoryImpl

    @Before
    fun setUp() {

        val local = LocalMockSource()
        val remote = RemoteMockSource()
        val sp = MockSharedPrefs()
        val aLocal = com.example.moodtracker.repository.account.LocalMockSource()
        val aRemote = com.example.moodtracker.repository.account.RemoteMockSource()

        solutionRepo = SolutionRepositoryImpl(local, remote, sp)
        accountRepo = AccountRepositoryImpl(aRemote, sp, aLocal)

    }

    @Test
    fun addSolution() = runBlocking {
        solutionRepo.addSolution(
            description = "Desc",
            advantages = "Adv",
            disadvantages = "Dis",
            worryDate = LocalDateTime.now()
        ).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data, `is`(2L))
        }
    }

    @Test
    fun getAllSolutionsForWorries() = runBlocking {
        solutionRepo.getAllSolutionsForWorries().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data, `is`(notNullValue()))
            assertThat(result.data!!.size, `is`(1))
        }
    }

    @Test
    fun getSolutionsForWorry() = runBlocking {
        solutionRepo.getSolutionsForWorry(LocalDateTime.of(2020, 3, 20, 0, 0)).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data, `is`(notNullValue()))
        }

        solutionRepo.getSolutionsForWorry(LocalDateTime.now()).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.data, `is`(nullValue()))
        }
    }

    @Test
    fun syncSolution() = runBlocking {
        solutionRepo.syncSolution(1).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
        }

        accountRepo.logout()

        solutionRepo.syncSolution(1).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message!!.toLowerCase(), containsString("server error"))
        }

    }
}