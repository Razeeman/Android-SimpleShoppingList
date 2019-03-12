package com.example.util.simpleshoppinglist.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Executors pool to run all disk IO tasks on the same thread.
 */
open class AppExecutors(val diskIO: Executor = DiskIOThreadExecutor(),
                        val mainThreadIO: Executor = MainThreadExecutor()) {

    /**
     * Executor that runs a task on a new background thread.
     */
    private class DiskIOThreadExecutor : Executor {

        private val diskIO = Executors.newSingleThreadExecutor()

        override fun execute(command: Runnable) { diskIO.execute(command) }
    }

    /**
     * Executor that runs a task on the main thread.
     */
    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}