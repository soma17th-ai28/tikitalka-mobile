package com.soma2026.tikitalka.presentation.chat

sealed class ChatIntent {
    data object LoadHistory : ChatIntent()
    data class UpdateInput(val text: String) : ChatIntent()
    data object SendMessage : ChatIntent()
    data class SelectSuggestedQuestion(val text: String) : ChatIntent()
}