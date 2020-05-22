package com.guralnya.notification_tracker.model.constants

import org.joda.time.DateTime

enum class Filtration(val filterValue: Long) {
    ALL_TIME(0),
    PER_HOUR(DateTime.now().minusHours(1).millis), //TODO need fix date update
    PER_DAY(DateTime.now().minusDays(1).millis),
    PER_MONTH(DateTime.now().minusMonths(1).millis)
}