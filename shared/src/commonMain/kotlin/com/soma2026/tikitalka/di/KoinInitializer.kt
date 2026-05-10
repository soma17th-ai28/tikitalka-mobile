package com.soma2026.tikitalka.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}): KoinApplication = startKoin {
    appDeclaration()
    modules(
        networkModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
    )
}