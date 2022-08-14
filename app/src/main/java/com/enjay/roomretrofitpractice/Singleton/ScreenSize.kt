package com.enjay.roomretrofitpractice.Singleton

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import com.enjay.roomretrofitpractice.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext


object ScreenSize {

    fun getScreenWidth(context : Context) = context.resources.displayMetrics.widthPixels

    fun getScreenHeight(context : Context) = context.resources.displayMetrics.heightPixels
}