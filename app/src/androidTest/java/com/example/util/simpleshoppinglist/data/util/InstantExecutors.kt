package com.example.util.simpleshoppinglist.data.util

import com.example.util.simpleshoppinglist.util.AppExecutors
import java.util.concurrent.Executor

/**
 * Allow instant execution of tasks.
 */
class InstantExecutors : AppExecutors(instant, instant) {
    companion object {
        private val instant = Executor { command -> command.run() }
    }
}