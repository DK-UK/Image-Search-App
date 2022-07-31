package com.enjay.roomretrofitpractice.APIModels

data class Result(
    val alt_description: String,
    val blur_hash: String,
    val categories: List<Any>,
    val color: String,
    val created_at: String,
    val likes: Int,
    val urls: Urls,
)