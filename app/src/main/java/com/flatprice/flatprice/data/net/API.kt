package com.flatprice.flatprice.data.net

import com.flatprice.flatprice.data.net.body.SearchResponse
import com.flatprice.flatprice.domain.SearchParam
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface API {

    @POST("get-price")
    fun getPrice(@Body searchParam: SearchParam): Single<SearchResponse>
}