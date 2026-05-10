package com.soma2026.tikitalka.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.soma2026.tikitalka.domain.model.Issue
import com.soma2026.tikitalka.presentation.dashboard.DashboardEffect
import com.soma2026.tikitalka.presentation.dashboard.DashboardIntent
import com.soma2026.tikitalka.presentation.dashboard.DashboardState
import com.soma2026.tikitalka.presentation.dashboard.DashboardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    onNavigateToChat: (issueId: String) -> Unit,
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DashboardEffect.NavigateToChat -> onNavigateToChat(effect.issueId)
                is DashboardEffect.ShowError -> { /* TODO: 스낵바 */ }
            }
        }
    }

    DashboardContent(
        state = state,
        onIssueClick = { issueId ->
            viewModel.handleIntent(DashboardIntent.SelectIssue(issueId))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardContent(
    state: DashboardState,
    onIssueClick: (issueId: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TikiTalka",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0),
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding),
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.issues.isEmpty() -> {
                    Text(
                        text = "뉴스를 불러오는 중입니다...",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF9E9E9E),
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                        items(state.issues, key = { it.id }) { issue ->
                            IssueCard(
                                issue = issue,
                                onClick = { onIssueClick(issue.id) },
                            )
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

// region Preview

private val previewIssues = listOf(
    Issue(
        id = "1",
        title = "음바페, 레알 마드리드와 결별설... 파리 복귀 가능성 제기",
        summary = "음바페가 레알 마드리드와의 불화설이 계속되는 가운데, 프랑스 현지 매체들이 파리 생제르맹 복귀 가능성을 연이어 보도하고 있다.",
        tag = "TRANSFER",
        publishedAt = "2시간 전",
        hotnessScore = 98,
        url = "",
        source = "L'Equipe",
    ),
    Issue(
        id = "2",
        title = "손흥민, 토트넘 잔류 확정... 새 계약 서명 임박",
        summary = "손흥민이 토트넘 홋스퍼와 새 계약 협상을 마무리하며 잔류가 사실상 확정됐다. 계약 기간은 2년으로 알려졌다.",
        tag = "CONTRACT",
        publishedAt = "5시간 전",
        hotnessScore = 91,
        url = "",
        source = "The Athletic",
    ),
    Issue(
        id = "3",
        title = "챔피언스리그 8강 대진 확정... 레알 vs 맨시티 빅매치 성사",
        summary = "UEFA 챔피언스리그 8강 대진 추첨 결과, 레알 마드리드와 맨체스터 시티가 맞대결을 펼치게 됐다.",
        tag = "UCL",
        publishedAt = "1일 전",
        hotnessScore = 85,
        url = "",
        source = "UEFA",
    ),
)

@Preview
@Composable
private fun DashboardContentPreview() {
    MaterialTheme {
        DashboardContent(
            state = DashboardState(issues = previewIssues),
            onIssueClick = {},
        )
    }
}

@Preview
@Composable
private fun DashboardContentLoadingPreview() {
    MaterialTheme {
        DashboardContent(
            state = DashboardState(isLoading = true),
            onIssueClick = {},
        )
    }
}

@Preview
@Composable
private fun DashboardContentEmptyPreview() {
    MaterialTheme {
        DashboardContent(
            state = DashboardState(),
            onIssueClick = {},
        )
    }
}

// endregion

@Composable
private fun IssueCard(
    issue: Issue,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp,
    ) {
        Column {
            // 썸네일 플레이스홀더
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF1565C0), Color(0xFF42A5F5)),
                        ),
                    ),
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // 태그 + 시간
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TagBadge(tag = issue.tag)
                    Text(
                        text = issue.publishedAt,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 제목
                Text(
                    text = issue.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 요약
                Text(
                    text = issue.summary,
                    fontSize = 14.sp,
                    color = Color(0xFF616161),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 출처 + 요약 보기
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = issue.source,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E),
                    )
                    Text(
                        text = "요약 보기 →",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun TagBadge(tag: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = tag.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1565C0),
        )
    }
}
