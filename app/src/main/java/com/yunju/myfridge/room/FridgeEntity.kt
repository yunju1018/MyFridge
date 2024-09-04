package com.yunju.myfridge.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yunju.myfridge.models.Product

@Entity (tableName = "fridgeTable")
class FridgeEntity (
    @PrimaryKey
    val id: String,
    val productList: List<Product>?       // todo : TypeConverter 적용
)