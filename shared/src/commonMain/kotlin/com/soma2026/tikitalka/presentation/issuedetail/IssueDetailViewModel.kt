package com.soma2026.tikitalka.presentation.issuedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soma2026.tikitalka.domain.usecase.GetIssueDetailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IssueDetailViewModel(
    private val getIssueDetail: GetIssueDetailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(IssueDetailState())
    val state: StateFlow<IssueDetailState> = _state.asStateFlow()

    private val _effect = Channel<IssueDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun load(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getIssueDetail(id)
                .onSuccess { issue ->
                    _state.update { it.copy(issue = issue, isLoading = false) }
                }
                .onFailure { error ->
                    val message = error.message ?: "뉴스를 불러오지 못했습니다"
                    _state.update { it.copy(isLoading = false, errorMessage = message) }
                    _effect.send(IssueDetailEffect.ShowError(message))
                }
        }
    }

    fun handleIntent(intent: IssueDetailIntent) {
        when (intent) {
            is IssueDetailIntent.NavigateBack -> {
                viewModelScope.launch { _effect.send(IssueDetailEffect.NavigateBack) }
            }
        }
    }
}