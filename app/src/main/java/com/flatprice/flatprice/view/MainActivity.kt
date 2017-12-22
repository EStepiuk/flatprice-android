package com.flatprice.flatprice.view

import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import com.flatprice.flatprice.R
import com.flatprice.flatprice.common.Mapper
import com.flatprice.flatprice.data.SearchRepositoryFactoryImpl
import com.flatprice.flatprice.domain.Search
import com.flatprice.flatprice.domain.SearchParam
import com.flatprice.flatprice.domain.SearchRepository
import com.flatprice.flatprice.extension.input
import com.flatprice.flatprice.extension.isEmpty
import com.flatprice.flatprice.extension.plusAssign
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar

class MainActivity : AppCompatActivity(), MainView {

    private val searchRepositoryFactory: SearchRepository.Factory by lazy { SearchRepositoryFactoryImpl(applicationContext) }
    private val mapper: Mapper<Search, SearchVM> = SearchVMMapper()

    private val disposables = CompositeDisposable()
    private val adapter = SearchesAdapter()

    private lateinit var spinnerAdapter: ArrayAdapter<String>

    private val items = mutableListOf<Search>()

    private var savedItemAndPosition: Pair<Search, Int>? = null

    private val undoSnackbarCallback = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)

            if (event != DISMISS_EVENT_ACTION) removeSavedItemFromDB()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()

        disposables += searchRepositoryFactory
                .getSearchRepository()
                .getCachedSearches()
                .subscribe { searches ->
                    items.addAll(searches)
                    addItems(searches.map(mapper::map))
                }
    }

    override fun onDestroy() {
        disposables.clear()

        super.onDestroy()
    }

    override fun setLoading(b: Boolean) {
        progressView.visibility = if (b) VISIBLE else GONE
    }

    override fun showError(throwable: Throwable) {
        val message = when (throwable) {
            else -> "Щось пішло не так"
        }

        snackbar(rootScrollView, message)
    }

    override fun showEnterDistrictMessage() {
        snackbar(rootScrollView, "Введіть район")
        etDistrict.requestFocus()
    }

    override fun showEnterSquareMessage() {
        snackbar(rootScrollView, "Введіть площу")
        etSquare.requestFocus()
    }

    override fun showEnterRoomsMessage() {
        snackbar(rootScrollView, "Введіть к-ть кімнат")
        etRoom.requestFocus()
    }

    override fun showUndoRemoveMessage() {
        longSnackbar(rootScrollView, "Впевнені що хочете видалити?", "Відмінити") {
            onUndoRemoveClick()
        }.apply {
            addCallback(undoSnackbarCallback)
        }
    }

    override fun addItems(items: List<SearchVM>, position: Int) {
        adapter.addItems(items, position)
    }

    override fun updateItem(position: Int, searchVM: SearchVM) {
        adapter.updateItem(position, searchVM)
    }

    private fun onBtnSearchClick() {
        when {
            etSquare.isEmpty -> showEnterSquareMessage()
            etRoom.isEmpty -> showEnterRoomsMessage()
            else -> {
                setLoading(true)
                disposables += searchRepositoryFactory
                        .getSearchRepository()
                        .search(SearchParam(spinnerAdapter.getItem(etDistrict.selectedItemPosition), etSquare.input.toInt(), etRoom.input.toInt()))
                        .subscribeBy(
                                onError = {
                                    setLoading(false)
                                    showError(it)
                                },
                                onSuccess = {
                                    items.add(0, it)

                                    setLoading(false)
                                    addItem(mapper.map(it))
                                }
                        )
            }
        }
    }

    private fun onItemRemoved(idx: Int) {
        savedItemAndPosition = Pair(items.removeAt(idx), idx)
        showUndoRemoveMessage()
    }

    private fun onUndoRemoveClick() {
        savedItemAndPosition?.let { (item, pos) ->
            items.add(pos, item)
            adapter.addItems(listOf(mapper.map(item)), pos)
        }
        savedItemAndPosition = null
    }

    private fun removeSavedItemFromDB() {
        savedItemAndPosition?.let {
            disposables += searchRepositoryFactory.getSearchRepository().deleteSearch(it.first).subscribe()
            savedItemAndPosition = null
        }
    }

    private fun setupView() {
        btnSearch.setOnClickListener { onBtnSearchClick() }
        setupDistrictSpinner()
        setupRecycler()
    }

    private fun setupDistrictSpinner() {
        spinnerAdapter = ArrayAdapter<String>(
                this,
                R.layout.spinner_drop_down_view,
                resources.getStringArray(R.array.districts)).apply {

            setDropDownViewResource(R.layout.spinner_drop_down_view)
        }
        etDistrict.adapter = spinnerAdapter
    }

    private fun setupRecycler() {
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onItemRemoved(viewHolder.adapterPosition)
                adapter.removeItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(recyclerView)
    }
}