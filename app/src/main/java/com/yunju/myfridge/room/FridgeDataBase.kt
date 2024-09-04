package com.yunju.myfridge.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yunju.myfridge.util.Converter

@Database(entities = [FridgeEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class FridgeDataBase : RoomDatabase() {
    abstract fun fridgeDao(): FridgeDao

    companion object {
        private var INSTANCE: FridgeDataBase? = null

        fun getInstance(context: Context): FridgeDataBase {
            return INSTANCE ?: synchronized(FridgeDataBase::class) {
                val instance = Room.databaseBuilder(
                context.applicationContext,
                FridgeDataBase::class.java,
                "fridge.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}