package com.project.lycommunity.util

sealed class ResultsWrapper<out T> {
    data class Success<out T>(val data: T) : ResultsWrapper<T>()
    data class Error(val exception: Throwable) : ResultsWrapper<Nothing>()
    data object Loading : ResultsWrapper<Nothing>()

}
