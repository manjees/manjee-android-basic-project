package com.manjee.basic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manjee.basic.data.local.entity.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(book: FavoriteBookEntity)

    @Query("DELETE FROM favorite_books WHERE id = :bookId")
    suspend fun delete(bookId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_books WHERE id = :bookId)")
    suspend fun isFavorite(bookId: String): Boolean

    @Query("SELECT id FROM favorite_books")
    fun observeFavoriteIds(): Flow<List<String>>

    @Query("SELECT * FROM favorite_books ORDER BY likedAt DESC")
    fun observeFavoriteBooks(): Flow<List<FavoriteBookEntity>>
}
