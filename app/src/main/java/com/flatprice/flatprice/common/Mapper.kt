package com.flatprice.flatprice.common


interface Mapper<in From, out To> {

    fun map(from: From): To
}