package com.soma2026.tikitalka.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module

fun initKoin(
    baseUrl: String,
    extraModules: List<Module> = emptyList(),
    appDeclaration: KoinApplication.() -> Unit = {},
): KoinApplication = startKoin {
    appDeclaration()
    modules(
        networkModule(baseUrl),
        repositoryModule,
        useCaseModule,
        viewModelModule,
        *extraModules.toTypedArray(),
    )
}