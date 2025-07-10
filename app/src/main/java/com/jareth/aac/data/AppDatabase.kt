package com.jareth.aac.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [TileEntity::class, CategoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tileDao(): TileDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aac_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let {
                                    prepopulateData(it.tileDao(), it.categoryDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateData(tileDao: TileDao, categoryDao: CategoryDao) {
            val categories = listOf(
                CategoryEntity(name = "Bathroom"),
                CategoryEntity(name = "Food"),
                CategoryEntity(name = "Emotions"),
                CategoryEntity(name = "Greetings"),
                CategoryEntity(name = "Needs")
            )
            categoryDao.insertAll(categories)

            val tiles = listOf(
                // Bathroom
                TileEntity(emoji = "🚽", label = "I need to go potty", category = "Bathroom"),
                TileEntity(emoji = "🧻", label = "I need toilet paper", category = "Bathroom"),
                TileEntity(emoji = "🛁", label = "I want a bath", category = "Bathroom"),
                TileEntity(emoji = "🧼", label = "Wash hands", category = "Bathroom"),

                // Food
                TileEntity(emoji = "🍎", label = "I want an apple", category = "Food"),
                TileEntity(emoji = "🍕", label = "I want pizza", category = "Food"),
                TileEntity(emoji = "🥤", label = "I’m thirsty", category = "Food"),
                TileEntity(emoji = "🍽️", label = "I’m hungry", category = "Food"),

                // Emotions
                TileEntity(emoji = "😃", label = "I'm happy", category = "Emotions"),
                TileEntity(emoji = "😢", label = "I'm sad", category = "Emotions"),
                TileEntity(emoji = "😠", label = "I'm angry", category = "Emotions"),
                TileEntity(emoji = "😴", label = "I'm tired", category = "Emotions"),

                // Greetings
                TileEntity(emoji = "👋", label = "Hello", category = "Greetings"),
                TileEntity(emoji = "🙋‍♂️", label = "How are you?", category = "Greetings"),
                TileEntity(emoji = "🙋‍♀️", label = "Good morning", category = "Greetings"),
                TileEntity(emoji = "👋", label = "Goodbye", category = "Greetings"),

                // Needs
                TileEntity(emoji = "❄️", label = "I’m cold", category = "Needs"),
                TileEntity(emoji = "🔥", label = "I’m hot", category = "Needs"),
                TileEntity(emoji = "🆘", label = "I need help", category = "Needs"),
                TileEntity(emoji = "💤", label = "I want to rest", category = "Needs")
            )
            tileDao.insertAll(tiles)
        }
    }
}
