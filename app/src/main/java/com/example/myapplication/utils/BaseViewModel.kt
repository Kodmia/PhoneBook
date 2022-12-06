package com.example.myapplication.utils

import androidx.lifecycle.ViewModel

/**
 * Базовая VM для реализации автозавершения задач при гибели VM
 */
open class BaseViewModel: ViewModel() {
    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        tasks.forEach {
            it.cancel()
        }
    }

    fun <T> Task<T>.autoCancel(){
        tasks.add(this)
    }
}