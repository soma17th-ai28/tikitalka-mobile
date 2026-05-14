package com.soma2026.tikitalka.domain.model

enum class League(val code: String, val displayName: String, val emblemUrl: String) {
    PREMIER_LEAGUE("PL", "프리미어리그", "https://crests.football-data.org/PL.png"),
    LA_LIGA("PD", "라리가", "https://crests.football-data.org/PD.png"),
    BUNDESLIGA("BL1", "분데스리가", "https://crests.football-data.org/BL1.png"),
    SERIE_A("SA", "세리에A", "https://crests.football-data.org/SA.png"),
    LIGUE_1("FL1", "리그앙", "https://crests.football-data.org/FL1.png"),
}