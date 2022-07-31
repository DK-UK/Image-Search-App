package com.enjay.roomretrofitpractice

import android.content.Context
import android.graphics.ImageDecoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enjay.roomretrofitpractice.APIModels.AllPhotosModelItem
import dagger.hilt.android.qualifiers.ApplicationContext

class PhotoAdapter(
    val context: Context,
    val arrayList: ArrayList<AllPhotosModelItem>
) : RecyclerView.Adapter<PhotoAdapter.viewHolder>() {

    inner class viewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val imageView : ImageView = view.findViewById(R.id.imgview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val allPhotosModelItem = arrayList[position]
        Glide.with(context).
                load(allPhotosModelItem.urls.raw)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return arrayList.count()
    }
}