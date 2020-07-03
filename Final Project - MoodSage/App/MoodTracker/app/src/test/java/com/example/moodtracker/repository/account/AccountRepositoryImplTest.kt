package com.example.moodtracker.repository.account

import com.example.moodtracker.data.database.models.entities.User
import com.example.moodtracker.data.network.models.UserRegisterDto
import com.example.moodtracker.repository.Status
import com.example.moodtracker.repository.data.sharedprefs.MockSharedPrefs
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class AccountRepositoryImplTest {

    private lateinit var repo : AccountRepository

    @Before
    fun setUp() {
        val local = LocalMockSource()
        val remote = RemoteMockSource()
        val sp = MockSharedPrefs()

        repo = AccountRepositoryImpl(remote, sp, local)
    }

    @Test
    fun getCurrentUser() = runBlocking {
       repo.getCurrentAccount().let { result ->
           assertThat(result.status, `is`(Status.SUCCESS))
           assertThat(result.data, `is`(notNullValue()))
           assertThat(result.data!!.forename, `is`("Test"))
       }

        repo.logout()
        repo.getCurrentAccount().let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.data, `is`(nullValue()))
        }
    }


    @Test
    fun acceptLink() = runBlocking {
        assertThat(repo.acceptLink("tid").status, `is`(Status.SUCCESS))

        assertThat(repo.acceptLink("potato").status, `is`(Status.FAILURE))
    }


    @Test
    fun removeLink() = runBlocking {
        assertThat(repo.removeLink("tid").status, `is`(Status.SUCCESS))
        assertThat(repo.removeLink("potato").status, `is`(Status.FAILURE))
    }

    @Test
    fun getLinks() = runBlocking {
        repo.getLinks().let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
            assertThat(result.data, `is`(notNullValue()))
            assertThat(result.data!!.size, `is`(1))
        }
    }

    @Test
    fun getLoginStatus() = runBlocking {
            assertThat(repo.getLoginStatus().data, `is`(true))
            repo.logout()
            assertThat(repo.getLoginStatus().data, `is`(false))
    }

    @Test
    fun login() = runBlocking {
        assertThat(repo.login("Test@User.com", "TestPassword").status, `is`(Status.SUCCESS))

        repo.login("Wrong", "Details").let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message!!.toLowerCase(), `is`("invalid email/password") )
        }

    }

    @Test
    fun logout() = runBlocking {
        assertThat(repo.logout().status, `is`(Status.SUCCESS))
    }

    @Test
    fun deleteAccount() = runBlocking {
        assertThat(repo.deleteAccount().status, `is`(Status.SUCCESS))
        repo.logout()
        assertThat(repo.deleteAccount().status, `is`(Status.FAILURE))
    }

    @Test
    fun register() = runBlocking {
        repo.createAccount(
            UserRegisterDto(
                forename = "New",
                surname = "Account",
                email = "new@acount.com",
                password = "testpassword"
        )).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
        }

        repo.createAccount(
            UserRegisterDto(
                forename = "Existing",
                surname = "Account",
                email = "Test@User.com",
                password = "testpassword"
            )
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
            assertThat(result.message, startsWith("Invalid details"))
        }

    }

    @Test
    fun updateAccount() = runBlocking {
        repo.updateAccount(
            User(
                id = "id",
                surname = "Test",
                forename = "Update",
                email = "Test@update.com"
            )
        ).let { result ->
            assertThat(result.status, `is`(Status.SUCCESS))
        }

        repo.logout()

        repo.updateAccount(
            User(
                id = "id",
                surname = "Test",
                forename = "Update",
                email = "Test@update.com"
            )
        ).let { result ->
            assertThat(result.status, `is`(Status.FAILURE))
        }
    }
}