package com.soma2026.tikitalka.presentation.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soma2026.tikitalka.domain.model.League
import com.soma2026.tikitalka.domain.usecase.GetStandingsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StandingsViewModel(
    private val getStandings: GetStandingsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(StandingsState())
    val state: StateFlow<StandingsState> = _state.asStateFlow()

    private val _effect = Channel<StandingsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        load(League.PREMIER_LEAGUE)
    }

    fun handleIntent(intent: StandingsIntent) {
        when (intent) {
            is StandingsIntent.SelectLeague -> selectLeague(intent.league)
            is StandingsIntent.Retry -> load(_state.value.selectedLeague)
        }
    }

    private fun selectLeague(league: League) {
        _state.update { it.copy(selectedLeague = league) }
        if (!_state.value.standings.containsKey(league)) {
            load(league)
        }
    }

    private fun load(league: League) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getStandings(league)
                .onSuccess { standings ->
                    _state.update {
                        it.copy(
                            standings = it.standings + (league to standings),
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    val message = error.message ?: "순위를 불러오지 못했습니다"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    _effect.send(StandingsEffect.ShowError(message))
                }
        }
    }
}