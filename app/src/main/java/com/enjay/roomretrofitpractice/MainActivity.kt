package com.enjay.roomretrofitpractice

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.enjay.roomretrofitpractice.APIModels.AllPhotosModel
import com.enjay.roomretrofitpractice.APIModels.SearchedPhoto
import com.enjay.roomretrofitpractice.APIModels.Urls
import com.enjay.roomretrofitpractice.hiltPratice.PhotoViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel: PhotoViewModel by viewModels()
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter
    private var arrayList: ArrayList<Urls> = arrayListOf()
    private lateinit var progressBar: ProgressBar
    private lateinit var noConnectionLinear : LinearLayout
    private lateinit var btnRetry : MaterialButton
    private var pageCount: Int = 1
    private var searchedText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photoRecyclerView = findViewById(R.id.photo_recycler)
        progressBar = findViewById(R.id.progress_bar)
        noConnectionLinear = findViewById(R.id.img_no_connection_linear)
        btnRetry = findViewById(R.id.btn_retry)

        photoRecyclerView.setHasFixedSize(true)
        photoRecyclerView.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        adapter = PhotoAdapter(this, arrayList)
        photoRecyclerView.adapter = adapter

        getAllPhotos()

        photoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    progressBar.visibility = View.VISIBLE

                    if (searchedText.isNotEmpty()) {
                        searchPhoto(searchedText)
                    } else {
                        getAllPhotos()
                    }
                }
            }
        })

        btnRetry.setOnClickListener {
            if (searchedText.isNotEmpty()){
                searchPhoto(searchedText)
            }
            else{
                getAllPhotos()
            }
        }
    }

    private fun getAllPhotos() {
        CoroutineScope(Dispatchers.Main).launch {

            if (isNetworkAvailable(applicationContext)) {
                val allPhotos = viewModel.getAllPhotos(pageCount)
                allPhotos.enqueue(object : retrofit2.Callback<AllPhotosModel> {
                    override fun onResponse(
                        call: Call<AllPhotosModel>,
                        response: Response<AllPhotosModel>
                    ) {
                        if (response.isSuccessful) {
                            photoRecyclerView.visibility = View.VISIBLE
                            noConnectionLinear.visibility = View.GONE

                            response.body()?.forEachIndexed { index, allPhotosModelItem ->
                                arrayList.add(allPhotosModelItem.urls)
                            }

                            progressBar.visibility = View.GONE
                            adapter.notifyDataSetChanged()
                            pageCount++
                        }
                    }

                    override fun onFailure(call: Call<AllPhotosModel>, t: Throwable) {
                        Log.e("Dhaval", "onResponse: url : ${t.message}")
                        progressBar.visibility = View.GONE
                    }
                })
            } else {
                photoRecyclerView.visibility = View.GONE
                noConnectionLinear.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun searchPhoto(searchedText: String) {

        CoroutineScope(Dispatchers.Main).launch {

            if (isNetworkAvailable(applicationContext)) {
                viewModel.getSearchedPhoto(searchedText, pageCount)
                    .enqueue(object : retrofit2.Callback<SearchedPhoto> {
                        override fun onResponse(
                            call: Call<SearchedPhoto>,
                            response: Response<SearchedPhoto>
                        ) {
                            if (response.isSuccessful) {
                                photoRecyclerView.visibility = View.VISIBLE
                                noConnectionLinear.visibility = View.GONE

                                Log.e("Dhaval", "onResponse: request : ${call.request()}")
                                response.body()?.results?.forEachIndexed { index, result ->

                                    Log.e(
                                        "Dhaval",
                                        "onResponse: index : $index --> ${result.urls.regular}"
                                    )
                                    arrayList.add(result.urls)
                                }
                                progressBar.visibility = View.GONE
                                adapter.notifyDataSetChanged()
                                pageCount++
                            }
                        }

                        override fun onFailure(call: Call<SearchedPhoto>, t: Throwable) {
                            Log.e("Dhaval", "onFailure: ${t.message} ")
                            progressBar.visibility = View.GONE
                        }

                    })
            } else {
                photoRecyclerView.visibility = View.GONE
                noConnectionLinear.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photo_menu, menu)

        val searchMenu = menu?.findItem(R.id.search)
        val gridViewMenu = menu?.findItem(R.id.grid_view)
        val listViewMenu = menu?.findItem(R.id.list_view)

        val searchView: SearchView = searchMenu?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {

                searchedText = text!!
                pageCount = 1
                arrayList.clear()
                searchPhoto(searchedText)
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                Log.e("Dhaval", "onQueryTextChange: $text")
                return false
            }

        })

        listViewMenu?.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                photoRecyclerView.layoutManager =
                    StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
                adapter = PhotoAdapter(this@MainActivity, arrayList)
                photoRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

                gridViewMenu?.isVisible = true
                listViewMenu.isVisible = false
                return false
            }

        })
        gridViewMenu?.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {


                photoRecyclerView.layoutManager =
                    StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                adapter = PhotoAdapter(this@MainActivity, arrayList)
                photoRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()

                listViewMenu?.isVisible = true
                gridViewMenu.isVisible = false
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

}