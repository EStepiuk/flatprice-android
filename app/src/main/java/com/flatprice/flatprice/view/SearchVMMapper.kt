package com.flatprice.flatprice.view

import com.flatprice.flatprice.common.Mapper
import com.flatprice.flatprice.domain.Search
import java.util.*


class SearchVMMapper : Mapper<Search, SearchVM> {

    override fun map(from: Search) = SearchVM(
            from.searchParam.district,
            from.searchParam.square,
            from.searchParam.rooms,
            Date(),
            from.searchResult.price
    )
}