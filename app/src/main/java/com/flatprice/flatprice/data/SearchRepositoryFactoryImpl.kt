package com.flatprice.flatprice.data

import android.arch.persistence.room.Room
import android.content.Context
import com.flatprice.flatprice.data.db.SearchDB
import com.flatprice.flatprice.data.net.API
import com.flatprice.flatprice.domain.SearchRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class SearchRepositoryFactoryImpl(context: Context) : SearchRepository.Factory {

    private val api: API
    private val db: SearchDB
    private val searchRepository: SearchRepository

    init {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        api = Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://f970ded7.ngrok.io/api/v1/")
                .build()
                .create(API::class.java)
        db = Room.databaseBuilder(context, SearchDB::class.java, "search-db").build()
        searchRepository = SearchRepositoryImpl(api, db)

    }

    override fun getSearchRepository() = searchRepository
}