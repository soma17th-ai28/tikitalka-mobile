package com.soma2026.tikitalka.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soma2026.tikitalka.domain.usecase.GetIssuesUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getIssues: GetIssuesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = Channel<DashboardEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(DashboardIntent.LoadIssues)
    }

    fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadIssues -> loadIssues()
            is DashboardIntent.SelectIssue -> navigateToChat(intent.issueId)
            is DashboardIntent.Refresh -> loadIssues()
        }
    }

    private fun loadIssues() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getIssues()
                .onSuccess { paged ->
                    _state.update { it.copy(issues = paged.content, isLoading = false, errorMessage = null) }
                }
                .onFailure { error ->
                    val message = error.message ?: "알 수 없는 오류"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    _effect.send(DashboardEffect.ShowError(message))
                }
        }
    }

    private fun navigateToChat(issueId: String) {
        viewModelScope.launch {
            _effect.send(DashboardEffect.NavigateToChat(issueId))
        }
    }
}
