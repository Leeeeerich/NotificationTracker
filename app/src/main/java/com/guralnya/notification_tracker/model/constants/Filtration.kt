package com.guralnya.notification_tracker.model.constants

import org.joda.time.DateTime

enum class Filtration {
    ALL_TIME,
    PER_HOUR,
    PER_DAY,
    PER_MONTH;

    fun getTimeFiltration(filtration: Filtration): Long {
        return when (filtration) {
            ALL_TIME -> 0
            PER_HOUR -> DateTime.now().minusHours(1).millis
            PER_DAY -> DateTime.now().minusDays(1).millis
            PER_MONTH -> DateTime.now().minusMonths(1).millis
        }
    }
}