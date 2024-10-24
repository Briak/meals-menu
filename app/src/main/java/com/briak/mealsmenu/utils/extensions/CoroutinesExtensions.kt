package com.briak.mealsmenu.utils.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CopyableThrowable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

@OptIn(DelicateCoroutinesApi::class)
fun <T> Flow<T>.defaultShareIn(timeout: Long = 60_000): SharedFlow<T> =
    this.shareIn(GlobalScope, SharingStarted.WhileSubscribed(timeout, 0), 1)

fun Throwable.isJobCancellation() = this is CancellationException && this is CopyableThrowable<*> && this !is TimeoutCancellationException
