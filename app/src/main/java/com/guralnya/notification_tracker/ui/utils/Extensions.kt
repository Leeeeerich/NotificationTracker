package com.guralnya.notification_tracker.ui.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun DateTime.getFormattedTime() = DateTimeFormat.shortTime().print(this)
fun DateTime.getFormattedDate() = DateTimeFormat.shortDate().print(this)