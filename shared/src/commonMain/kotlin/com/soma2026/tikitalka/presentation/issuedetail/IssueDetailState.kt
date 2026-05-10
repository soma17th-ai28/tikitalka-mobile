package com.soma2026.tikitalka.presentation.issuedetail

import com.soma2026.tikitalka.domain.model.Issue

data class IssueDetailState(
    val issue: Issue? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)