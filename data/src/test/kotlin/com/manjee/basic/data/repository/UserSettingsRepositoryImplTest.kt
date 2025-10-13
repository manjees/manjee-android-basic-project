package com.manjee.basic.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.manjee.basic.data.datastore.UserSettingsSerializer
import com.manjee.basic.data.proto.UserSettingsProto
import com.manjee.basic.domain.model.AppLanguage
import com.manjee.basic.domain.model.ThemeMode
import java.nio.file.Files
import java.nio.file.Path
import java.util.Locale
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope

class UserSettingsRepositoryImplTest {

    private lateinit var tmpDir: Path
    private lateinit var dataStore: DataStore<UserSettingsProto>
    private lateinit var repository: UserSettingsRepositoryImpl
    private lateinit var originalLocale: Locale
    private val testScope = TestScope(StandardTestDispatcher())

    @BeforeTest
    fun setUp() {
        originalLocale = Locale.getDefault()
        Locale.setDefault(Locale.US)
        tmpDir = Files.createTempDirectory("user_settings_test")
        val serializer = UserSettingsSerializer()
        dataStore = DataStoreFactory.create(
            serializer = serializer,
            scope = testScope,
            produceFile = { tmpDir.resolve("user_settings.pb").toFile() }
        )
        repository = UserSettingsRepositoryImpl(dataStore)
    }

    @AfterTest
    fun tearDown() {
        testScope.cancel()
        if (tmpDir.exists()) {
            tmpDir.listDirectoryEntries().forEach { path ->
                if (path.isDirectory()) {
                    path.listDirectoryEntries().forEach { it.deleteIfExists() }
                }
                path.deleteIfExists()
            }
            tmpDir.deleteIfExists()
        }
        Locale.setDefault(originalLocale)
    }

    @Test
    fun setThemeMode_persistsSelection() = testScope.runTest {
        repository.setThemeMode(ThemeMode.LIGHT)

        val settings = repository.observeUserSettings().first()

        assertEquals(ThemeMode.LIGHT, settings.themeMode)
    }

    @Test
    fun setLanguage_persistsSelection() = testScope.runTest {
        repository.setLanguage(AppLanguage.KOREAN)

        val settings = repository.observeUserSettings().first()

        assertEquals(AppLanguage.KOREAN, settings.language)
    }

    @Test
    fun observeUserSettings_emitsDefaultWhenUnset() = testScope.runTest {
        val settings = repository.observeUserSettings().first()

        assertEquals(ThemeMode.DARK, settings.themeMode)
        assertEquals(AppLanguage.ENGLISH, settings.language)
    }
}
