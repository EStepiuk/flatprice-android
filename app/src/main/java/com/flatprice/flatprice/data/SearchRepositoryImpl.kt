package com.flatprice.flatprice.data

import com.flatprice.flatprice.data.db.SearchDB
import com.flatprice.flatprice.data.db.SearchEntity
import com.flatprice.flatprice.data.db.SearchMapper
import com.flatprice.flatprice.data.net.API
import com.flatprice.flatprice.domain.Search
import com.flatprice.flatprice.domain.SearchParam
import com.flatprice.flatprice.domain.SearchRepository
import com.flatprice.flatprice.domain.SearchResult
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class SearchRepositoryImpl(private val api: API, private val searchDB: SearchDB) : SearchRepository {

    override fun search(searchParam: SearchParam): Single<Search> {
        return api
                .getPrice(searchParam)
                .flatMap { searchResult ->
                    Single.defer {
                        val entity = SearchEntity(searchParam.district, searchParam.square, searchParam.rooms, Date(), searchResult.result)
                        searchDB.dao().insert(entity)
                        Single.just(entity)
                    }
                }
                .map { SearchMapper.map(it) }
                .compose(applySchedulers())
    }

    override fun getCachedSearches(): Single<List<Search>> {
        return Single
                .fromCallable { searchDB.dao().queryAll() }
                .map { it.map { SearchMapper.map(it) } }
                .compose(applySchedulers())
    }

    override fun deleteSearch(search: Search): Completable {
        return Completable
                .fromCallable { searchDB.dao().delete(SearchEntity(id = search.id)) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateSearch(search: Search, searchResult: SearchResult): Completable {
        return Completable.complete()
    }

    private fun <T> applySchedulers() = SingleTransformer<T, T> { it.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()) }
}