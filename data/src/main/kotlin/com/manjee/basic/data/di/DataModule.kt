package com.manjee.basic.data.di

import com.manjee.basic.data.repository.BookRepositoryImpl
import com.manjee.basic.data.repository.LikedBooksRepositoryImpl
import com.manjee.basic.data.repository.UserSettingsRepositoryImpl
import com.manjee.basic.domain.repository.BookRepository
import com.manjee.basic.domain.repository.LikedBooksRepository
import com.manjee.basic.domain.repository.UserSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(bookRepositoryImpl: BookRepositoryImpl): BookRepository

    @Binds
    @Singleton
    abstract fun bindLikedBooksRepository(likedBooksRepositoryImpl: LikedBooksRepositoryImpl): LikedBooksRepository

    @Binds
    @Singleton
    abstract fun bindUserSettingsRepository(userSettingsRepositoryImpl: UserSettingsRepositoryImpl): UserSettingsRepository
}
