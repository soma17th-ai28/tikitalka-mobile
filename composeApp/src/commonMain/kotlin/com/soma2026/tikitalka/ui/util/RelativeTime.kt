package com.soma2026.tikitalka.ui.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * ISO 8601 문자열을 상대 시간 문자열로 변환합니다.
 *
 * 예) "2024-01-15T10:30:00Z" → "3시간 전"
 *
 * 파싱 실패 시 원본 문자열을 그대로 반환합니다.
 */
fun String.toRelativeTimeString(): String {
    return try {
        val instant = Instant.parse(this)
        val now = Clock.System.now()
        val seconds = (now - instant).inWholeSeconds

        when {
            seconds < 60       -> "방금 전"
            seconds < 3_600    -> "${seconds / 60}분 전"
            seconds < 86_400   -> "${seconds / 3_600}시간 전"
            seconds < 604_800  -> "${seconds / 86_400}일 전"
            seconds < 2_592_000 -> "${seconds / 604_800}주 전"
            seconds < 31_536_000 -> "${seconds / 2_592_000}달 전"
            else               -> "${seconds / 31_536_000}년 전"
        }
    } catch (_: Exception) {
        this
    }
}