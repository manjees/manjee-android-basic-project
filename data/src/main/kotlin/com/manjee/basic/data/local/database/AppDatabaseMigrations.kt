package com.manjee.basic.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
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
                    `likedAt` INTEGER NOT NULL,
                    PRIMARY KEY(`id`)
                )
                """.trimIndent()
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE `favorite_books` ADD COLUMN `likedAt` INTEGER NOT NULL DEFAULT 0")
            database.execSQL("UPDATE `favorite_books` SET `likedAt` = strftime('%s','now') * 1000 WHERE `likedAt` = 0")
        }
    }
}
