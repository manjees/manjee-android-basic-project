package com.manjee.basic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.manjee.basic.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE `query` = :query")
    suspend fun getRemoteKey(query: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)
}
