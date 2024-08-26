package com.yunju.myfridge

import android.app.Application
import com.yunju.myfridge.room.FridgeDataBase


class MyApplication: Application() {
    private val db by lazy { FridgeDataBase.getInstance(this)}
    val fridgeDao by lazy {db.fridgeDao()}
}