package com.example.footballleagueapp

import AreaWithCompetitions
import Competition
import Root
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header


class FootballViewModel : ViewModel() {
    // API Configuration
    private companion object {
        const val BASE_URL = "https://api.football-data.org/v4/"
        const val API_TOKEN = "c20b612f2b2446eb9016001e0ffd4658"
    }

    // State
    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state

    // Retrofit Service Interface
    private interface FootballApiService {
        @GET("competitions")
        suspend fun getCompetitions(
            @Header("X-Auth-Token") apiKey: String
        ): Root
    }

    // Retrofit Instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Service
    private val apiService: FootballApiService by lazy {
        retrofit.create(FootballApiService::class.java)
    }

    init {
        fetchCompetitions()
    }

    private val _selectedCompetition = MutableStateFlow<Competition?>(null)
    val selectedCompetition: StateFlow<Competition?> = _selectedCompetition

    fun selectCompetition(competition: Competition) {
        _selectedCompetition.value = competition
    }

    fun clearSelectedCompetition() {
        _selectedCompetition.value = null
    }

    private val _competitions = mutableStateListOf<Competition>()
    val competitions: List<Competition> get() = _competitions

    // Update your fetch method to group by area
    fun fetchCompetitions() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val response = apiService.getCompetitions(API_TOKEN)
                _competitions.clear()
                _competitions.addAll(response.competitions)
                // Group competitions by area for the home screen
                val areas = response.competitions
                    .groupBy { it.area }
                    .map { (area, comps) ->
                        AreaWithCompetitions(area.name, area.code, comps.sortedBy { it.name })
                    }
                    .sortedBy { it.name }
                _state.value = UiState.Success(areas)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Error loading competitions"){}
            }
        }
    }


    fun getCompetitionById(id: Int): Competition? {
        return _competitions.firstOrNull { it.id.toInt() == id }
    }}