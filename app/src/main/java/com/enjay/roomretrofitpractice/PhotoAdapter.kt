package com.enjay.roomretrofitpractice

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.findViewTreeOnBackPressedDispatcherOwner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enjay.roomretrofitpractice.APIModels.Urls
import com.enjay.roomretrofitpractice.ShowFullImage.FullScreenImage
import com.enjay.roomretrofitpractice.Singleton.DownloadAndSaveImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.Permission

class PhotoAdapter(
    val context: Activity,
    val arrayList: ArrayList<Urls>
) : RecyclerView.Adapter<PhotoAdapter.viewHolder>() {

    private lateinit var downloadAndSaveImage: DownloadAndSaveImage
    private lateinit var wallpaperManager: WallpaperManager
    private lateinit var fullScreenImage: FullScreenImage

    init {
        downloadAndSaveImage = DownloadAndSaveImage.getInstance(context)
        wallpaperManager = WallpaperManager.getInstance(context)
        fullScreenImage = FullScreenImage.getInstance(context)
    }

    inner class viewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView = view.findViewById(R.id.imgview)
        val imgViewDownload : ImageView = view.findViewById(R.id.imgview_download)
        val progressDownloading : ProgressBar = view.findViewById(R.id.progressbar_downloading)
        val imgViewWallpaper : ImageView = view.findViewById(R.id.imgview_wallpaper)
        val imgViewFullScreen : ImageView = view.findViewById(R.id.imgview_full_screen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val photoUrls = arrayList[position]
        Glide.with(context).
                load(photoUrls.regular)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)

        // Download Image
        holder.imgViewDownload.setOnClickListener {

            holder.progressDownloading.visibility = View.VISIBLE
            holder.imgViewDownload.visibility = View.GONE

            CoroutineScope(Dispatchers.Main).launch {
                val isDownloaded = downloadAndSaveImage.downloadImage(photoUrls.regular)
                if(isDownloaded) {
                    holder.progressDownloading.visibility = View.GONE
                    holder.imgViewDownload.visibility = View.VISIBLE
                }
                else {
                    holder.progressDownloading.visibility = View.VISIBLE
                    holder.imgViewDownload.visibility = View.GONE
                }
            }
        }

        // To set Image as a Wallpaper
        holder.imgViewWallpaper.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                wallpaperManager.setBitmap(downloadAndSaveImage.getBitmapFromURL(photoUrls.regular))
            }
            Toast.makeText(context, "wallpaper set!!", Toast.LENGTH_SHORT).show()
        }

        holder.imgViewFullScreen.setOnClickListener {
            fullScreenImage.showFullScreenImageDialog(photoUrls.regular)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.count()
    }

}