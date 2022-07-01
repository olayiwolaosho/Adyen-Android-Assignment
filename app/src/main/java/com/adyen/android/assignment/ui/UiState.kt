package com.adyen.android.assignment.ui

//will use this for my UI state instead of resource
sealed class UiState<out T>{

    data class Success<out T>(val data: T): UiState<T>()

    data class Error(val message: String): UiState<Nothing>()

    object Loading : UiState<Nothing>()

    object Empty : UiState<Nothing>()
}
