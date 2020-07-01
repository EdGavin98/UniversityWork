package com.example.moodtracker.data.network

import android.accounts.NetworkErrorException
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import java.nio.channels.NotYetConnectedException

/***
 * Wrapper for data returned from the app, includes response code and status of the call,
 * allows for easier network error handling within the repository
 */
data class ApiCall<out T>(
    val state: ApiCallState,
    val data: T?,
    val code: Int?
) {
    companion object {
        fun <T> success(data: T?, code: Int): ApiCall<T> {
            return ApiCall(ApiCallState.SUCCESS, data, code)
        }

        fun <T> error(code: Int): ApiCall<T> {
            return ApiCall(ApiCallState.ERROR, null, code)
        }

        fun <T> networkError(): ApiCall<T> {
            return ApiCall(ApiCallState.NETWORK_ERROR, null, null)
        }

    }
}

/***
 * Helper function to make a retrofit call and convert Response to ApiCall
 * @param call - Retrofit call function to perform
 * @return ApiCall object with status of call, code and relevant data
 */
suspend fun <T> makeCall(call: suspend () -> Response<T>): ApiCall<T> {
    return try {
        call().run {
            if (isSuccessful) {
                ApiCall.success(body(), code())
            } else {
                ApiCall.error(code())
            }
        }
    } catch (e: NetworkErrorException) {
        ApiCall.networkError()
    } catch (e: NotYetConnectedException) {
        ApiCall.networkError()
    } catch (e: ConnectException) {
        ApiCall.networkError()
    } catch (e: UnknownHostException) {
        ApiCall.networkError()
    }
}