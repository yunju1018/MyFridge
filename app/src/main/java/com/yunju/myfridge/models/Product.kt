package com.yunju.myfridge.models

import java.io.Serializable

data class Product(
    var productName: String? = null,    // 상품명
    var dateAdded: String? = null,      // 입고일
    var dateExpire: String? = null      // 유통기한
) : Serializable
