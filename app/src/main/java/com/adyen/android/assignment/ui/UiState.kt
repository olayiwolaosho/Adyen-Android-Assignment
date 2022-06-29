package com.adyen.android.assignment.ui

sealed class UiState<out T>{

    data class Success<out T>(val data: T): UiState<T>()

    data class Error(val message: String): UiState<Nothing>()

    object Loading : UiState<Nothing>()

    object Empty : UiState<Nothing>()
}
