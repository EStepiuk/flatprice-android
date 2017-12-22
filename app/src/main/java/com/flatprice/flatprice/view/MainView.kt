package com.flatprice.flatprice.view


interface MainView {

    fun setLoading(b: Boolean)

    fun showError(throwable: Throwable)

    fun showEnterDistrictMessage()

    fun showEnterSquareMessage()

    fun showEnterRoomsMessage()

    fun showUndoRemoveMessage()

    fun addItem(item: SearchVM, position: Int = 0) {
        addItems(listOf(item), position)
    }

    fun addItems(items: List<SearchVM>, position: Int = 0)

    fun updateItem(position: Int, searchVM: SearchVM)
}