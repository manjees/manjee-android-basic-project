package com.manjee.basic.data.local.database

import android.database.Cursor
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertTrue
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
            3,
            true,
            AppDatabaseMigrations.MIGRATION_1_2,
            AppDatabaseMigrations.MIGRATION_2_3
        )

        migratedDb.query("PRAGMA table_info(`favorite_books`)").use { cursor ->
            assertColumnExists(cursor, "id", notNull = true)
            assertColumnExists(cursor, "authors", notNull = true)
            assertColumnExists(cursor, "likedAt", notNull = true)
        }
    }

    @Test
    fun migrate2To3_backfillsLikedAt() {
        migrationHelper.createDatabase(testDbName, 2).apply {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS `favorite_books` (
                    `id` TEXT NOT NULL,
                    `title` TEXT NOT NULL,
                    `authors` TEXT NOT NULL,
                    `publisher` TEXT,
                    `publishedDate` TEXT,
                    `description` TEXT,
                    `pageCount` INTEGER,
                    `averageRating` REAL,
                    `ratingsCount` INTEGER,
                    `thumbnail` TEXT,
                    `infoLink` TEXT,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
            execSQL(
                """
                INSERT INTO `favorite_books`
                (`id`, `title`, `authors`, `publisher`, `publishedDate`, `description`, `pageCount`, `averageRating`, `ratingsCount`, `thumbnail`, `infoLink`)
                VALUES ('id-1', 'Title', 'Author', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)
                """.trimIndent()
            )
            close()
        }

        val migratedDb: SupportSQLiteDatabase = migrationHelper.runMigrationsAndValidate(
            testDbName,
            3,
            true,
            AppDatabaseMigrations.MIGRATION_2_3
        )

        migratedDb.query("SELECT likedAt FROM `favorite_books` WHERE id = 'id-1'").use { cursor ->
            assertTrue(cursor.moveToFirst())
            val likedAt = cursor.getLong(0)
            assertTrue("likedAt should be backfilled with a timestamp", likedAt > 0L)
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
