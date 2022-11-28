/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    // AndroidViewModel is the same as ViewModel but takes the application context as a parameter
    //      application context gives access to resources
    application: Application
) : AndroidViewModel(application) {

    /**
     * The following is no longer recommended because of ViewModel's lifecycle awareness
     */
    // allows canceling all coroutines started by this view model when the view model is destroyed
//    private var viewModelJob = Job()
//
//    // called when the view model is destroyed
//    override fun onCleared() {
//        super.onCleared()
//
//        // cancel all the coroutines
//        viewModelJob.cancel()
//    }
//
//    // UI scope
//    //  Coroutine launches in the UI Scope will run on the main thread
//    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // hold the current night
    private val tonight = MutableLiveData<SleepNight?>()

    // Hold all the nights
    private val nights = database.getAllNights()

    // content of the data
    val nightString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        // get tonight from the database without blocking UI
        viewModelScope.launch { // launching coroutine creates the coroutine without blocking the current thread in the context defined by the scope
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            // returns the latest night saved in the database
            var night = database.getTonight()

            // If the start and end time are the same, then we know we are continuing from an existing night
            // if time not the same, then we have no night started
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }

            // return the night
            night
        }
    }

    // For start button click
    fun onStartTracking() {
        viewModelScope.launch {
            // create new sleep night
            val newNight = SleepNight()

            // insert the new sleep night into the database
            insert(newNight)

            // assign it to tonight
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {

        // Don't need withContext because ViewModels provide their own scope by default
        withContext(Dispatchers.IO) {
            database.insert(night)
        }
    }

    // Stop button
    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    // Clear button
    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }


}

