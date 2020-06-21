package com.wiz.moviedb.database

import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Database
import com.wiz.moviedb.BaseApp
import com.wiz.moviedb.domain.MovieModel

@Database(entities = arrayOf(MovieModel::class),
    version = 1, exportSchema = false)
abstract class RoomDB: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private val DB_NAME="wiz-database"
        private var INSTANCE: RoomDB? = null

        fun getAppDatabase(context: Context): RoomDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext as BaseApp,
                    RoomDB::class.java, DB_NAME)
                        // allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
//                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE as RoomDB
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}