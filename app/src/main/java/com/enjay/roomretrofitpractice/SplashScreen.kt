package com.enjay.roomretrofitpractice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.enjay.roomretrofitpractice.Singleton.ScreenSize


class SplashScreen : AppCompatActivity() {

    private lateinit var splashScreenImg : ImageView
    private lateinit var splashScreenTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        splashScreenImg = findViewById(R.id.splash_screen_img)
        splashScreenTxt = findViewById(R.id.splash_screen_txt)

        val width = ScreenSize.getScreenWidth(this)
        val height = ScreenSize.getScreenHeight(this)

        val params = RelativeLayout.LayoutParams((width - 200), (height / 2))
        params.addRule(RelativeLayout.CENTER_IN_PARENT, 1)
        splashScreenImg.layoutParams = params

        splashScreenImg.animate().translationX(dipToPixels(applicationContext, -0f)).rotation(360f).setDuration(1500).start()
        splashScreenTxt.animate().translationX(dipToPixels(applicationContext, 0f)).rotation(360f).setDuration(1500).start()

        Handler(mainLooper).postDelayed(Runnable {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }, 2000)
    }
    fun dipToPixels(context : Context, dipValue: Float): Float {
        val metrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}