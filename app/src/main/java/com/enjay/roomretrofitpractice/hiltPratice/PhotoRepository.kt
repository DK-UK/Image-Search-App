package com.enjay.roomretrofitpractice.hiltPratice

import android.util.Log
import com.enjay.roomretrofitpractice.APIModels.AllPhotosModel
import com.enjay.roomretrofitpractice.APIModels.Result
import com.enjay.roomretrofitpractice.APIModels.SearchedPhoto
import com.enjay.roomretrofitpractice.APIModels.Urls
import retrofit2.Call
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val apiInterface: APIInterface, private val clientKey : String) {

    suspend fun searchPhoto(searchStr : String, pageCount : Int) : Call<SearchedPhoto> {
        return apiInterface.searchPhoto(search = searchStr, client_key = clientKey, page = pageCount)
    }

    suspend fun getAllPhotos(page : Int) : Call<AllPhotosModel> {
        Log.e("Dhaval", "getAllPhotos: CLIENT KEY : $clientKey", )
        return apiInterface.getAllPhotos(client_key = clientKey, page = page)
    }
}