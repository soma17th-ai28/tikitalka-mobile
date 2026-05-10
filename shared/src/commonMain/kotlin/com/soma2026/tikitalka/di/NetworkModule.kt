package com.soma2026.tikitalka.di

import com.soma2026.tikitalka.data.remote.api.ChatApi
import com.soma2026.tikitalka.data.remote.api.IssueApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "localhost"
                    port = 8080
                }
            }
        }
    }
    single { IssueApi(get()) }
    single { ChatApi(get()) }
}