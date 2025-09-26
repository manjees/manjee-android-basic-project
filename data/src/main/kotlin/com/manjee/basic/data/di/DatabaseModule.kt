package com.manjee.basic.data.di

import android.content.Context
import androidx.room.Room
import com.manjee.basic.data.local.dao.FavoriteBookDao
import com.manjee.basic.data.local.database.AppDatabase
import com.manjee.basic.data.local.database.AppDatabaseMigrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "book_database"
        ).addMigrations(
            AppDatabaseMigrations.MIGRATION_1_2,
            AppDatabaseMigrations.MIGRATION_2_3
        )
            .build()
    }

    @Provides
    fun provideBookDao(database: AppDatabase) = database.bookDao()

    @Provides
    fun provideRemoteKeyDao(database: AppDatabase) = database.remoteKeyDao()

    @Provides
    fun provideFavoriteBookDao(database: AppDatabase): FavoriteBookDao = database.favoriteBookDao()
}
