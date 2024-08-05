package org.jojo.sleep_tracker.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Job
import org.jojo.sleep_tracker.database.SleepDatabaseDao

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    // access to the database
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {
        private var viewModelJob = Job()


}