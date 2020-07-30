package com.ptrv.news.domain

data class News(
    val url: String,
    val title: String,
    val imageUrl: String?,
    val description: String,
    val date: String
)