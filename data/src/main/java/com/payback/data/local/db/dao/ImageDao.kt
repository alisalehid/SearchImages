package com.payback.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.payback.data.local.Image


@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<Image>)


    @Query("SELECT * FROM image_table WHERE searchTerm LIKE :query")
    fun queryImage(query: String): PagingSource < Int, Image>

    @Query("DELETE FROM image_table")
    suspend fun clearAll()

    @Query("SELECT * FROM image_table")
    suspend fun getAll(): List<Image>



}