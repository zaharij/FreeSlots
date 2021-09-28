package com.calendar.freeslots.model.exception

import java.lang.RuntimeException

class ViewModelNotSubscribedException: RuntimeException() {
    override val message: String
        get() = "ViewModel does not subscribed on the repository"
}