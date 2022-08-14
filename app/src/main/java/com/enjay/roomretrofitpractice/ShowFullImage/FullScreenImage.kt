package com.enjay.roomretrofitpractice.ShowFullImage

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.enjay.roomretrofitpractice.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.view.ViewGroup

import android.view.Gravity





class FullScreenImage(val activity: Activity) {

    companion object{
        private var INSTANCE : FullScreenImage ? = null

        fun getInstance(activity: Activity) : FullScreenImage {
            if (INSTANCE == null){
                synchronized(this){
                    return FullScreenImage(activity)
                }
            }
            return INSTANCE!!
        }
    }

    fun showFullScreenImageDialog(urlString : String) {
        val view = LayoutInflater.from(activity).inflate(R.layout.full_screen_image_dialog, null)
        val imgViewFullScreen = view.findViewById(R.id.imgview_full_screen_img) as ImageView
        val fabClose = view.findViewById(R.id.fab_close) as FloatingActionButton

        Glide.with(activity).
        load(urlString)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(imgViewFullScreen)

        val builder = MaterialAlertDialogBuilder(activity, R.style.RoundedCornersDialog)
        builder.setView(view)

        val dialog = builder.create()

        fabClose.setOnClickListener{
            dialog.dismiss()
        }

        val window = dialog.window
        val wlp : WindowManager.LayoutParams? = window?.attributes

        wlp?.gravity = Gravity.CENTER
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.attributes = wlp
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }
}