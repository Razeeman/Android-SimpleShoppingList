package com.example.util.simpleshoppinglist.util

import java.util.concurrent.Executor

/**
 * Allow instant execution of tasks. Used in tests.
 */
class InstantExecutors : AppExecutors(instant, instant) {
    companion object {
        private val instant = Executor { command -> command.run() }
    }
}