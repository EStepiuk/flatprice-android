package com.flatprice.flatprice.view

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flatprice.flatprice.R
import com.flatprice.flatprice.common.DateFormats
import com.flatprice.flatprice.extension.format
import kotlinx.android.synthetic.main.search_cell.view.*


class SearchesAdapter : RecyclerView.Adapter<SearchVH>() {

    private val _items = mutableListOf<SearchVM>()
    val items get() = _items as List<SearchVM>

    fun addItems(newItems: List<SearchVM>, position: Int = 0) {
        _items.addAll(position, newItems)
        notifyItemRangeInserted(position, newItems.size)
    }

    fun removeItem(position: Int) {
        _items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(position: Int, item: SearchVM) {
        _items.removeAt(position)
        _items.add(0, item)
        notifyItemMoved(position, 0)
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            SearchVH(LayoutInflater.from(parent?.context).inflate(R.layout.search_cell, parent, false))

    override fun onBindViewHolder(holder: SearchVH?, position: Int) {
        val item = items[position]
        with(holder?.itemView ?: throw IllegalArgumentException("ViewHolder can't be null")) {
            val accentColor = ContextCompat.getColor(context, R.color.colorAccent)

            tvDistrict.text = spanWithAccent("район: ", item.district, accentColor)
            tvSquare.text = spanWithAccent("площа: ", item.square.toString(), accentColor)
            tvRooms.text = spanWithAccent("кімнат: ", item.rooms.toString(), accentColor)
            tvDate.text = DateFormats.ddmmYYYY format item.date
            tvPrice.text = item.price.toString()
        }
    }

    override fun getItemCount() = items.size
}

class SearchVH(itemView: View) : RecyclerView.ViewHolder(itemView)

private fun spanWithAccent(normal: String, accent: String, color: Int): SpannableStringBuilder {
    val ssb = SpannableStringBuilder(normal)
    val spanFrom = ssb.length
    ssb.append(accent)
    ssb.setSpan(ForegroundColorSpan(color), spanFrom, ssb.length, 0)
    return ssb
}