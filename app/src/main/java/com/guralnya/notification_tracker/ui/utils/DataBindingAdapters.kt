package com.guralnya.notification_tracker.ui.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.joda.time.DateTime

object DataBindingAdapters {

    @BindingAdapter("app:time")
    @JvmStatic
    fun time(view: TextView?, value: DateTime?) {
        view?.text = value?.getFormattedTime()
    }

    @BindingAdapter("app:date")
    @JvmStatic
    fun date(view: TextView?, value: DateTime?) {
        view?.text = value?.getFormattedDate()
    }
}