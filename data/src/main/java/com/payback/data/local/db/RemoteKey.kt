package com.payback.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_key")
data class RemoteKey(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageId: Int,
    val previousPage: Int?,
    val nextPage: Int?
) {
}