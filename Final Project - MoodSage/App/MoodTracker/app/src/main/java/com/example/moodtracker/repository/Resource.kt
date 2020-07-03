package com.example.moodtracker.repository


/**
 *  Generic wrapper for a repository response, holds the value and the status of the call
 *  alongside an error message that can be displayed to the user if needed
 *
 *  Adapted version of Google's Resource class from the GithubBrowserSample
 *  Source: https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
 */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> failure(message: String?, data: T?): Resource<T> {
            return Resource(Status.FAILURE, data, message)
        }
    }
}
