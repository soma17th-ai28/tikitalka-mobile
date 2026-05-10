package com.soma2026.tikitalka.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(
    baseUrl: String,
    appDeclaration: KoinApplication.() -> Unit = {},
): KoinApplication = startKoin {
    appDeclaration()
    modules(
        networkModule(baseUrl),
        repositoryModule,
        useCaseModule,
        viewModelModule,
    )
}