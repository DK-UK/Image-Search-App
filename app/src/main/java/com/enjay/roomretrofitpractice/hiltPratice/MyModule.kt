package com.enjay.roomretrofitpractice.hiltPratice

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {

    const val BASE_URL = "https://api.unsplash.com/"

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit) : APIInterface = retrofit.create(APIInterface::class.java)

    @Provides
    @Singleton
    fun provideClientKey() = "vjkMrtHpLSZqHbDsKsww1xChrOQS_DtNoeK7Wezh2GI"
}