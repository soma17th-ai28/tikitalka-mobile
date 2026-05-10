package com.soma2026.tikitalka.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soma2026.tikitalka.domain.model.ChatMessage
import com.soma2026.tikitalka.domain.model.MessageRole
import com.soma2026.tikitalka.domain.usecase.GetChatHistoryUseCase
import com.soma2026.tikitalka.domain.usecase.GetDeviceIdUseCase
import com.soma2026.tikitalka.domain.usecase.SendChatMessageUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendChatMessage: SendChatMessageUseCase,
    private val getChatHistory: GetChatHistoryUseCase,
    private val getDeviceId: GetDeviceIdUseCase,
) : ViewModel() {

    private val deviceId: String by lazy { getDeviceId() }

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _effect = Channel<ChatEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(ChatIntent.LoadHistory)
    }

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.LoadHistory -> loadHistory()
            is ChatIntent.UpdateInput -> _state.update { it.copy(inputText = intent.text) }
            is ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.SelectSuggestedQuestion -> _state.update { it.copy(inputText = intent.text) }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingHistory = true) }
            getChatHistory(deviceId)
                .onSuccess { messages ->
                    _state.update { it.copy(messages = messages, isLoadingHistory = false) }
                }
                .onFailure {
                    _state.update { it.copy(isLoadingHistory = false) }
                }
        }
    }

    private fun sendMessage() {
        val text = _state.value.inputText.trim()
        if (text.isBlank() || _state.value.isSending) return

        val userMessage = ChatMessage(
            role = MessageRole.USER,
            content = text,
            suggestedQuestion = null,
            createdAt = "",
        )

        _state.update {
            it.copy(
                messages = it.messages + userMessage,
                inputText = "",
                isSending = true,
            )
        }

        viewModelScope.launch {
            sendChatMessage(deviceId, text)
                .onSuccess { response ->
                    _state.update {
                        it.copy(
                            messages = it.messages + response,
                            isSending = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isSending = false) }
                    _effect.send(ChatEffect.ShowError(error.message ?: "메시지 전송에 실패했습니다"))
                }
        }
    }
}