package com.enjay.roomretrofitpractice.hiltPratice

import com.enjay.roomretrofitpractice.APIModels.AllPhotosModel
import com.enjay.roomretrofitpractice.APIModels.Result
import com.enjay.roomretrofitpractice.APIModels.Urls
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIInterface {

    @GET("/search/photos/")
    fun searchPhoto(@Query("query") search : String, @Query("client_id") client_key : String) : Call<Result>

    @GET("/photos")
    fun getAllPhotos(@Query("client_id") client_key : String) : Call<AllPhotosModel>
}