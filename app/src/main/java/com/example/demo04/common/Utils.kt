package com.example.demo04.common
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun convertTimestampToDateString(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}