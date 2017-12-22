package com.flatprice.flatprice.domain

import io.reactivex.Completable
import io.reactivex.Single


interface SearchRepository {

    interface Factory {

        fun getSearchRepository(): SearchRepository
    }

    fun search(searchParam: SearchParam): Single<Search>

    fun getCachedSearches(): Single<List<Search>>

    fun deleteSearch(search: Search): Completable

    fun updateSearch(search: Search, searchResult: SearchResult): Completable
}