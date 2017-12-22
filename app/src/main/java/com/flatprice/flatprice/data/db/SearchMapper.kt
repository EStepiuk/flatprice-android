package com.flatprice.flatprice.data.db

import com.flatprice.flatprice.common.Mapper
import com.flatprice.flatprice.domain.Search
import com.flatprice.flatprice.domain.SearchParam
import com.flatprice.flatprice.domain.SearchResult


object SearchMapper : Mapper<SearchEntity, Search> {

    override fun map(from: SearchEntity) = Search(
            from.id,
            SearchParam(from.district!!, from.square!!, from.room!!),
            SearchResult(from.price!!)
    )
}