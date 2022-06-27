package com.payback.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.payback.data.local.db.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_key WHERE imageId = :id")
    suspend fun remoteKeysImageId(id: Int): RemoteKey?

    @Query("DELETE FROM remote_key")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM remote_key")
    suspend fun getAll(): List<RemoteKey>
}