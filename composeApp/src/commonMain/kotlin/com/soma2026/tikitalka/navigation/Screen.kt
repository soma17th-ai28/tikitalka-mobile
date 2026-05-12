package com.soma2026.tikitalka.navigation

import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Chat : Screen("chat")
}

@Serializable
data class IssueDetailRoute(val id: String)
