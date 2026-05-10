package com.soma2026.tikitalka.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soma2026.tikitalka.navigation.Screen
import com.soma2026.tikitalka.ui.dashboard.DashboardScreen
import com.soma2026.tikitalka.ui.theme.TikiTalkaTheme

@Composable
fun App() {
    TikiTalkaTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToChat = { issueId ->
                        navController.navigate(Screen.Chat.createRoute(issueId))
                    },
                )
            }
            composable(Screen.Chat.route) {
                // TODO: #6 Chat UI
            }
        }
    }
}
