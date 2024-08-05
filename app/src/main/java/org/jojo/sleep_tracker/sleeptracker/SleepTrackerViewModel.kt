package org.jojo.sleep_tracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jojo.sleep_tracker.database.SleepDatabaseDao
import org.jojo.sleep_tracker.database.SleepNight

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    // access to the database
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {
        private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    private val nights = database.getAllNights()

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
       uiScope.launch{
           tonight.value = getTonightFromDatabase()
       }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) night = null

            night
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}