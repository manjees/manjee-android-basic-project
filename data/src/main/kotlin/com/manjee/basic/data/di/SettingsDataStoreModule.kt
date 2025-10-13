package com.manjee.basic.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.manjee.basic.data.datastore.UserSettingsSerializer
import com.manjee.basic.data.proto.UserSettingsProto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsDataStoreModule {

    private const val DATA_STORE_FILE_NAME = "user_settings.pb"

    @Provides
    @Singleton
    fun provideUserSettingsDataStore(
        @ApplicationContext context: Context,
        userSettingsSerializer: UserSettingsSerializer
    ): DataStore<UserSettingsProto> {
        return DataStoreFactory.create(
            serializer = userSettingsSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) }
        )
    }
}
