package com.example.moodtracker.repository.worry

import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.repository.account.AccountRepositoryImpl
import com.example.moodtracker.repository.data.sharedprefs.MockSharedPrefs
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime

class WorryRepositoryImplTest {

    private lateinit var worryRepository : WorryRepository
    private lateinit var accountRepository : AccountRepository

    @Before
    fun setUp() {

        val local = LocalMockSource()
        val remote = RemoteMockSource()
        val sp = MockSharedPrefs()

        val aLocal = com.example.moodtracker.repository.account.LocalMockSource()
        val aRemote = com.example.moodtracker.repository.account.RemoteMockSource()

        worryRepository = WorryRepositoryImpl(local, remote, sp)
        accountRepository = AccountRepositoryImpl(aRemote, sp, aLocal)

    }

    @Test
    fun getCurrentAndHypotheticalCount() = runBlocking {
        worryRepository.getCurrentAndHypotheticalCount().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            result.data!!.forEach { item ->
                assertThat(item.type, anyOf(`is`("Current"), `is`("Hypothetical")))
                if (item.type == "Current")  {
                    assertThat(item.count, `is`(2))
                } else {
                    assertThat(item.count, `is`(1))
                }
            }
        }
    }

    @Test
    fun syncWorry() = runBlocking {

        assertThat(worryRepository.syncWorry(LocalDateTime.of(2020, 4, 1, 0, 0)).status, `is`(Status.SUCCESS))

        worryRepository.syncWorry(LocalDateTime.of(2020, 4, 7, 0, 0)).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("No Worry"))
        }

        accountRepository.logout()
        worryRepository.syncWorry(LocalDateTime.of(2020, 4, 1, 0, 0)).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("Server"))
        }

    }

    @Test
    fun getAllWorrySeverities() = runBlocking {
        worryRepository.getAllWorrySeverities().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data!!.size, `is`(3))
        }

        worryRepository.addNewWorry(LocalDateTime.now(), 4, "d", "Current")
        worryRepository.getAllWorrySeverities().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data!!.size, `is`(4))
        }
    }

    @Test
    fun getWorrySeverityAverageByDay() = runBlocking {
        worryRepository.getWorrySeverityAverageByDay().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data!!.size, `is`(7))
            assertThat(result.data!![2], `is`(5f))
        }
    }

    @Test
    fun getAllWorries() = runBlocking {
        assertThat(worryRepository.getAllWorries().status, `is`(Status.SUCCESS))
    }

    @Test
    fun getWorryByDate() = runBlocking {
        worryRepository.getWorryByDate(LocalDateTime.of(2020, 4, 1, 0, 0)).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data!!.severity, `is`(4))
        }

        worryRepository.getWorryByDate(LocalDateTime.of(2020, 9, 1, 0, 0)).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("No worry"))
        }
    }

    @Test
    fun getLatestWorries() = runBlocking {
        assertThat(worryRepository.getLatestWorries().status, `is`(Status.SUCCESS))
        accountRepository.logout()
        assertThat(worryRepository.getLatestWorries().status, `is`(Status.FAILURE))
    }

    @Test
    fun addNewWorry() = runBlocking {

        worryRepository.addNewWorry(
            date = LocalDateTime.now(),
            type = "Current",
            severity = 4,
            description = "Test"
        ).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
        }

        worryRepository.addNewWorry(
            date = LocalDateTime.of(2020, 4, 1, 0, 0),
            type = "Current",
            severity = 4,
            description = "Test"
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }

        worryRepository.addNewWorry(
            date = LocalDateTime.now(),
            type = "NotAType",
            severity = 4,
            description = "Test"
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }

        worryRepository.addNewWorry(
            date = LocalDateTime.now(),
            type = "Current",
            severity = 12,
            description = "Test"
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }

        worryRepository.addNewWorry(
            date = LocalDateTime.now(),
            type = "Current",
            severity = 12,
            description = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest"
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }


    }
}