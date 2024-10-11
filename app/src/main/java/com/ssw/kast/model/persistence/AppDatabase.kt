package com.ssw.kast.model.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssw.kast.model.persistence.dao.IRecentSongDao
import com.ssw.kast.model.persistence.dao.IUserDao
import com.ssw.kast.model.persistence.dao.RecentSongDao
import com.ssw.kast.model.persistence.dao.UserDao

@Database(
    entities = [
        RecentSongDao::class,
        UserDao::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentSongDao(): IRecentSongDao
    abstract fun userDao(): IUserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}