package com.enjay.roomretrofitpractice.APIModels

data class SearchedPhoto(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)