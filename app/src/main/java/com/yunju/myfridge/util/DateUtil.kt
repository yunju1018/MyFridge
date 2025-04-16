package com.yunju.myfridge.util

import java.text.SimpleDateFormat
import java.util.Locale

class DateUtil {

    companion object {
        val FORMAT_DATE_YYYY_YEAR_MM_MONTH_DD_DAY = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
    }
}