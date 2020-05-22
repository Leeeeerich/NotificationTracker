package com.guralnya.notification_tracker.ui.custom_view

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu

abstract class BaseDropDownMenuAdapter<E : Enum<*>>(context: Context, view: View) :
    PopupMenu(context, view) {
    private val data = mutableListOf<E>()
    var selected: Int = 0
    var selectListener: ((E) -> Unit)? = null

    abstract fun getText(item: E): String?

    init {
        setOnMenuItemClickListener {
            selectListener?.invoke(data[it.itemId])
            selected = it.order
            true
        }
    }

    fun setData(data: List<E>) {
        this.data.clear()
        this.data.addAll(data)
        for ((i, item) in data.withIndex()) {
            menu.add(0, i, i, getText(item))
        }
    }
}