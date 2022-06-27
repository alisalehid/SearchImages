package com.payback.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.payback.data.Constants.DATABASE_NAME
import com.payback.data.local.db.dao.ImageDao
import com.payback.data.local.Image
import com.payback.data.local.db.dao.RemoteKeyDao
import javax.inject.Singleton


/**
 * for cache images
 * use Room on SqLite
 * */


@Database(

    entities = [Image::class, RemoteKey::class],
    version = 1, exportSchema = false
)
@Singleton
abstract class LocalDB : RoomDatabase() {

    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun imageDao(): ImageDao

    companion object {

        @Volatile
        private var instance: LocalDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {

                instance ?: buildDatabase(
                    context
                ).also {
                    instance = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LocalDB::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
    }
}