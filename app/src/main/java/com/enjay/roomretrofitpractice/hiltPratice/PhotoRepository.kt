package com.enjay.roomretrofitpractice.hiltPratice

import android.util.Log
import com.enjay.roomretrofitpractice.APIModels.AllPhotosModel
import com.enjay.roomretrofitpractice.APIModels.Result
import com.enjay.roomretrofitpractice.APIModels.SearchedPhoto
import com.enjay.roomretrofitpractice.APIModels.Urls
import retrofit2.Call
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val apiInterface: APIInterface, private val clientKey : String) {

    suspend fun searchPhoto(searchStr : String) : Call<SearchedPhoto> {
        return apiInterface.searchPhoto(searchStr, clientKey)
    }

    suspend fun getAllPhotos() : Call<AllPhotosModel> {
        Log.e("Dhaval", "getAllPhotos: CLIENT KEY : $clientKey", )
        return apiInterface.getAllPhotos(clientKey)
    }
}