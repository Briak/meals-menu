package com.briak.mealsmenu.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.briak.mealsmenu.utils.extensions.isJobCancellation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(DelicateCoroutinesApi::class)
abstract class BaseViewModel : ViewModel() {
    private val throttle = AtomicBoolean(false)

    private val managedJobList: MutableList<Job> = mutableListOf()
    private val managedEventJobList: MutableList<Job> = mutableListOf()
    private val localJobByUid: MutableMap<String, Job> = mutableMapOf()
    private val globalJobByUid: MutableMap<String, Job> = mutableMapOf()

    protected fun <T> Flow<T>.launchAndManageJob() {
        this.launch().manageJob()
    }

    protected fun <T> Flow<T>.launch(): Job =
        this
            .catch { error ->
                if (error.isJobCancellation().not()) {
                    Timber.d(error)
                }
            }.flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)

    protected fun Job.manageJob() {
        managedJobList.add(this)
    }

    protected fun disposeJobs() {
        managedJobList.toList().forEach { job -> job.cancel() }
        managedJobList.clear()
    }

    protected fun <T> Flow<T>.launchAndManageEventJob() {
        val job =
            this
                .catch { error -> Timber.e(error) }
                .flowOn(Dispatchers.Default)
                .launchIn(viewModelScope)
        managedEventJobList.add(job)
    }

    protected fun disposeEventJobs() {
        managedEventJobList.toList().forEach { job -> job.cancel() }
        managedEventJobList.clear()
    }

    protected fun launchWithBlocking(block: suspend () -> Unit) =
        launch {
            if (throttle.compareAndSet(false, true)) {
                try {
                    block()
                } finally {
                    throttle.set(false)
                }
            }
        }

    protected fun launch(
        uid: String? = null,
        block: suspend () -> Unit,
    ) {
        if (uid != null) {
            localJobByUid[uid]?.cancel()
            localJobByUid[uid] = launchJob(block)
        } else {
            launchJob(block)
        }
    }

    protected fun launchJob(block: suspend () -> Unit) =
        viewModelScope
            .launch(Dispatchers.Default) {
                try {
                    block()
                } catch (error: Exception) {
                    if (error.isJobCancellation().not()) {
                        Timber.e(error)
                        throw error
                    }
                }
            }

    protected fun launchGlobal(
        uid: String? = null,
        block: suspend () -> Unit,
    ) {
        if (uid != null) {
            globalJobByUid[uid]?.cancel()
            globalJobByUid[uid] = launchGlobalJob(block)
        } else {
            launchGlobalJob(block)
        }
    }

    private fun launchGlobalJob(block: suspend () -> Unit) =
        GlobalScope
            .launch(Dispatchers.Default) {
                try {
                    block()
                } catch (error: Exception) {
                    if (error.isJobCancellation().not()) {
                        Timber.e(error)
                    }
                }
            }
}
