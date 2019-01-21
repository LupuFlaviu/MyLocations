package com.flaviul.test.mylocations.utils

sealed class Result<T> {
    data class Progress<T>(var loading: Boolean) : Result<T>()
    data class Success<T>(var data: T) : Result<T>()
    data class Failure<T>(val error: Throwable) : Result<T>()

    companion object {
        fun <T> loading(isLoading: Boolean): Result<T> = Progress(isLoading)

        fun <T> success(data: T): Result<T> = Success(data)

        fun <T> failure(error: Throwable): Result<T> = Failure(error)
    }
}