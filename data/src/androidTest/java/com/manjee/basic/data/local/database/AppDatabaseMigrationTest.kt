package com.manjee.basic.data.local.database

import android.database.Cursor
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseMigrationTest {

    private val testDbName = "migration-test-db"

    @get:Rule
    val migrationHelper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @After
    fun tearDown() {
        ApplicationProvider.getApplicationContext<android.content.Context>().deleteDatabase(testDbName)
    }

    @Test
    fun migrate1To2_createsFavoriteBooksTableWithNotNullAuthors() {
        migrationHelper.createDatabase(testDbName, 1).apply {
            close()
        }

        val migratedDb: SupportSQLiteDatabase = migrationHelper.runMigrationsAndValidate(
            testDbName,
            2,
            true,
            AppDatabaseMigrations.MIGRATION_1_2
        )

        migratedDb.query("PRAGMA table_info(`favorite_books`)").use { cursor ->
            assertColumnExists(cursor, "id", notNull = true)
            assertColumnExists(cursor, "authors", notNull = true)
        }
    }

    private fun assertColumnExists(cursor: Cursor, columnName: String, notNull: Boolean) {
        val nameIndex = cursor.getColumnIndex("name")
        val notNullIndex = cursor.getColumnIndex("notnull")
        while (cursor.moveToNext()) {
            val name = cursor.getString(nameIndex)
            if (name == columnName) {
                if (notNull) {
                    require(cursor.getInt(notNullIndex) == 1) { "Column $columnName expected to be NOT NULL" }
                }
                return
            }
        }
        throw AssertionError("Column $columnName not found in table")
    }
}
