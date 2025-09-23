package com.manjee.basic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.manjee.basic.data.local.dao.BookDao
import com.manjee.basic.data.local.dao.FavoriteBookDao
import com.manjee.basic.data.local.dao.RemoteKeyDao
import com.manjee.basic.data.local.entity.BookEntity
import com.manjee.basic.data.local.entity.FavoriteBookEntity
import com.manjee.basic.data.local.entity.RemoteKeyEntity

@Database(
    entities = [BookEntity::class, RemoteKeyEntity::class, FavoriteBookEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun favoriteBookDao(): FavoriteBookDao
}
