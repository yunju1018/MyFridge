package com.yunju.myfridge.models

import java.io.Serializable

data class Product(
    val productName: String? = null,    // 상품명
    val dateAdded: String? = null,      // 입고일
    val dateExpire: String? = null      // 유통기한
) : Serializable
