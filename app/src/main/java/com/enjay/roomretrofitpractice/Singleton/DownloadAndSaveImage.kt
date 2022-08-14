package com.enjay.roomretrofitpractice.Singleton

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class DownloadAndSaveImage(val context: Activity) {

    companion object {
        private var INSTANCE: DownloadAndSaveImage? = null

        fun getInstance(context: Activity): DownloadAndSaveImage {
            if (INSTANCE == null) {
                synchronized(this) {
                    return DownloadAndSaveImage(context)
                }
            }
            return INSTANCE!!
        }
    }

   suspend fun downloadImage(urlString: String) : Boolean {
        val fileDownloaded = CoroutineScope(Dispatchers.IO).async {

            createFile(urlString)
        }
       return fileDownloaded.await()
    }

    fun createFile(urlString: String): Boolean {
        try {

            val url = URL(urlString).openConnection().getInputStream()
            val inputStream = URL(urlString).openStream()

//           val inputStream: InputStream? = context.getContentResolver().openInputStream(selectedFileUri)
            val newfile: File? = context.getExternalFilesDir("ImageGallery")
            val outputFile = File(newfile, "${System.currentTimeMillis()}.jpg")
            val os = FileOutputStream(outputFile)
            val buffer = ByteArray(256)
            var count: Int
            if (inputStream != null) {
                while (inputStream.read(buffer).also { count = it } > 0) {
                    os.write(buffer, 0, count)
                }
                inputStream.close()
                return true
            }
            os.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun getBitmapFromURL(urlStr : String) : Bitmap?{
        try {
            val url = URL(urlStr)
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            return image
        } catch (e: IOException) {
            println(e)
        }
        return null
    }
}