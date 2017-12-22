package com.flatprice.flatprice.extension


import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

inline fun TextView.textWatcher(init: KTextWatcher.() -> Unit) = addTextChangedListener(KTextWatcher().apply(init))

val TextView.isEmpty
    get() = text.isEmpty()

val TextView.isNotEmpty
    get() = text.isNotEmpty()

val TextView.isBlank
    get() = text.isBlank()

val TextView.isNotBlank
    get() = text.isNotBlank()

val EditText.input get() = text.toString()

class KTextWatcher : TextWatcher {

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _afterTextChanged: ((Editable?) -> Unit)? = null
    private var _afterTextChangedString: ((String) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable) {
        _afterTextChanged?.invoke(s)
        _afterTextChangedString?.invoke(s.toString())
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    fun afterChanged(listener: (String) -> Unit) {
        _afterTextChangedString = listener
    }

}