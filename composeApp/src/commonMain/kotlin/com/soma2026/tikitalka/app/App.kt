package com.soma2026.tikitalka.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.soma2026.tikitalka.navigation.Screen
import com.soma2026.tikitalka.ui.chat.ChatScreen
import com.soma2026.tikitalka.ui.dashboard.DashboardScreen
import com.soma2026.tikitalka.ui.theme.TikiTalkaTheme

@Composable
fun App() {
    TikiTalkaTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Column(modifier = Modifier.fillMaxSize().imePadding()) {
            Box(modifier = Modifier.weight(1f)) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Dashboard.route,
                ) {
                    composable(Screen.Dashboard.route) {
                        DashboardScreen()
                    }
                    composable(Screen.Chat.route) {
                        ChatScreen()
                    }
                }
            }

            NavigationBar(
                modifier = Modifier.height(56.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                val isDashboard = currentRoute == Screen.Dashboard.route
                val isChat = currentRoute == Screen.Chat.route

                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                NavigationBarItem(
                    selected = isDashboard,
                    onClick = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isDashboard) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "피드",
                        )
                    },
                    label = { Text("피드", style = MaterialTheme.typography.labelSmall) },
                    colors = itemColors,
                )
                NavigationBarItem(
                    selected = isChat,
                    onClick = {
                        navController.navigate(Screen.Chat.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isChat) Icons.Filled.Email else Icons.Outlined.Email,
                            contentDescription = "채팅",
                        )
                    },
                    label = { Text("채팅", style = MaterialTheme.typography.labelSmall) },
                    colors = itemColors,
                )
            }
        }
    }
}