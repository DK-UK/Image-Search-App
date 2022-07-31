package com.enjay.roomretrofitpractice.hiltPratice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enjay.roomretrofitpractice.APIModels.AllPhotosModel
import com.enjay.roomretrofitpractice.APIModels.Result
import com.enjay.roomretrofitpractice.APIModels.SearchedPhoto
import com.enjay.roomretrofitpractice.APIModels.Urls
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(private val repository: PhotoRepository) : ViewModel() {

    suspend fun getSearchedPhoto(searchedStr : String) : Call<SearchedPhoto> {
        val deffered = viewModelScope.async {
            repository.searchPhoto(searchedStr)
        }
        return deffered.await()
    }

    suspend fun getAllPhotos() : Call<AllPhotosModel> {
        val deffered = viewModelScope.async {
            repository.getAllPhotos()
        }
        return deffered.await()
    }
}