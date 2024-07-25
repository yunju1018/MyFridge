package com.yunju.myfridge.models

import java.io.Serializable

data class Product(
    val productName: String,    // 상품명
    val dateAdded: String,      // 입고일
    val dateExpire: String      // 유통기한
) : Serializable
