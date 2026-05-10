package com.soma2026.tikitalka.presentation.dashboard

import com.soma2026.tikitalka.domain.model.Issue

data class DashboardState(
    val issues: List<Issue> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
