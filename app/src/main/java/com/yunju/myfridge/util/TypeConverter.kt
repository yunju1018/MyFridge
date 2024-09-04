package com.yunju.myfridge.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.yunju.myfridge.models.Product

class Converter {

    @TypeConverter
    fun productListToJson(product: List<Product>): String {
        return Gson().toJson(product)
    }

    @TypeConverter
    fun jsonToProductList(productJson: String): List<Product> {
        return Gson().fromJson(productJson, Array<Product>::class.java).toList()
    }
}