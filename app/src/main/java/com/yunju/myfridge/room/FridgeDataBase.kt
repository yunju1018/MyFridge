package com.yunju.myfridge.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FridgeEntity::class], version = 1)
abstract class FridgeDataBase : RoomDatabase() {
    abstract fun fridgeDao(): FridgeDao

    companion object {
        private var INSTANCE: FridgeDataBase? = null

        fun getInstance(context: Context): FridgeDataBase? {
            if (INSTANCE == null) {
                synchronized(FridgeDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FridgeDataBase::class.java,
                        "fridge.db"
                    ).build()
                }
            }

            return INSTANCE
        }
    }
}