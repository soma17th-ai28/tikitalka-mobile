package com.soma2026.tikitalka.presentation.issuedetail

sealed class IssueDetailIntent {
    data object NavigateBack : IssueDetailIntent()
}