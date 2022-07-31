package com.enjay.roomretrofitpractice

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.enjay.roomretrofitpractice.APIModels.AllPhotosModel
import com.enjay.roomretrofitpractice.APIModels.Result
import com.enjay.roomretrofitpractice.APIModels.Urls
import com.enjay.roomretrofitpractice.hiltPratice.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Url
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: PhotoViewModel by viewModels<PhotoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getSearchedPhoto("dog").enqueue(object : retrofit2.Callback<Result>{
                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    Log.e("Dhaval", "onResponse: ${response.body()?.urls}", )
                }

                override fun onFailure(call: Call<Result>, t: Throwable) {

                }

            })
        }

        CoroutineScope(Dispatchers.Main).launch {
           val allPhotos = viewModel.getAllPhotos()
            allPhotos.enqueue(object : retrofit2.Callback<AllPhotosModel>{
                override fun onResponse(call: Call<AllPhotosModel>, response: Response<AllPhotosModel>) {

                    response.body()?.forEachIndexed { index, allPhotosModelItem ->
                        Log.e("Dhaval", "index $index --> ${allPhotosModelItem.urls.raw}", )
                    }
                }

                override fun onFailure(call: Call<AllPhotosModel>, t: Throwable) {
                    Log.e("Dhaval", "onResponse: url : ${t.message}")
                }

            })
        }
    }
}