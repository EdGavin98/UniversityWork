package com.example.moodtracker.repository.mood

import com.example.moodtracker.data.database.models.entities.Mood
import com.example.moodtracker.data.database.models.returnobjects.DateRating
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.account.AccountRepository
import com.example.moodtracker.repository.account.AccountRepositoryImpl
import com.example.moodtracker.repository.data.sharedprefs.MockSharedPrefs
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

class MoodRepositoryImplTest {

    private lateinit var moodRepository : MoodRepository
    private lateinit var accountRepository : AccountRepository

    @Before
    fun setUp() {
        val local = LocalMockSource()
        val remote = RemoteMockSource()
        val sp = MockSharedPrefs()

        val aLocal = com.example.moodtracker.repository.account.LocalMockSource()
        val aRemote = com.example.moodtracker.repository.account.RemoteMockSource()

        moodRepository = MoodRepositoryImpl(remote, local, sp)
        accountRepository = AccountRepositoryImpl(aRemote, sp, aLocal)
    }

    @Test
    fun getAllMoodRatings() = runBlocking {
        moodRepository.getAllMoodRatings().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assert(result.data is List<DateRating>)
            assertThat(result.data!!.size, `is`(3))
        }
    }

    @Test
    fun getLatestMoods() = runBlocking {
        assertThat(moodRepository.getLatestMoods().status, `is`(Status.SUCCESS))
        accountRepository.logout()
        assertThat(moodRepository.getLatestMoods().status, `is`(Status.FAILURE))

    }

    @Test
    fun getAverageMoodRatingByDay() = runBlocking {

        moodRepository.getAverageMoodRatingByDay().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assert(result.data is FloatArray)
            assertThat(result.data!!.size, `is`(7))
            assertThat(result.data!![2], `is`(5f))
        }
    }

    @Test
    fun getMoodByDate() = runBlocking {

        moodRepository.getMoodByDate(LocalDate.of(2020, 4, 1).atStartOfDay()).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assert(result.data is Mood)
            assertThat(result.data!!.rating, `is`(4))
        }

        moodRepository.getMoodByDate(LocalDate.of(2020, 4, 3).atStartOfDay()).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("No mood"))
        }

    }

    @Test
    fun getMoodsFromMonth() = runBlocking {
        moodRepository.getMoodsFromMonth(LocalDate.of(2020, 4, 1)).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data!!.size, `is`(3))
        }

        moodRepository.getMoodsFromMonth(LocalDate.of(2020, 5, 1)).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("No moods"))
        }

    }

    @Test
    fun syncMood() = runBlocking {

        assertThat(moodRepository.syncMood(LocalDate.of(2020, 4, 1).atStartOfDay()).status, `is`(Status.SUCCESS))

        moodRepository.syncMood(LocalDate.of(2020, 4, 4).atStartOfDay()).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("No Mood"))
        }

        accountRepository.logout()
        moodRepository.syncMood(LocalDate.of(2020, 4, 1).atStartOfDay()).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("Server"))
        }

    }

    @Test
    fun addMood() = runBlocking {

        moodRepository.addMood(
            date = LocalDate.of(2020, 5, 1).atStartOfDay(),
            comment = "Test",
            rating = 4,
            isPrivate = false
        ).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
        }
        moodRepository.addMood(
            date = LocalDate.of(2020, 5, 1).atStartOfDay(),
            comment = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest",
            rating = 12,
            isPrivate = false
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }

        moodRepository.addMood(
            date = LocalDate.of(2020, 5, 1).atStartOfDay(),
            comment = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest",
            rating = 12,
            isPrivate = false
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }

        moodRepository.addMood(
            date = LocalDate.of(2020, 4, 1).atStartOfDay(),
            comment = "Test",
            rating = 5,
            isPrivate = false
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }
    }
}