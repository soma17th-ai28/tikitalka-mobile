package com.soma2026.tikitalka.presentation.dashboard

sealed class DashboardEffect {
    data class NavigateToChat(val issueId: String) : DashboardEffect()
    data class ShowError(val message: String) : DashboardEffect()
}
