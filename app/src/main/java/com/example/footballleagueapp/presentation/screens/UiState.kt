package com.example.footballleagueapp.presentation.screens

import AreaWithCompetitions

sealed class UiState {
    object Loading : UiState()
    data class Success(val areas: List<AreaWithCompetitions>) : UiState()
    data class Error(val message: String, val retryAction: () -> Unit) : UiState()
    companion object
}