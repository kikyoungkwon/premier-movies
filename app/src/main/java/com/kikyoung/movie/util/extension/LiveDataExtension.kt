package com.kikyoung.movie.util.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeChanges(owner: LifecycleOwner, onChanged: (T) -> Unit) {
    observe(owner,
        Observer {
            onChanged(it)
        })
}