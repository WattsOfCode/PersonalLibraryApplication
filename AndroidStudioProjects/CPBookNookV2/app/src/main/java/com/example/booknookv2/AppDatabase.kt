package com.example.booknookv2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// entities
@Database(
    entities = [User::class, Book::class, Bookcase::class, Loan::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun bookcaseDao(): BookcaseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "book_nook_db"
                )

                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
