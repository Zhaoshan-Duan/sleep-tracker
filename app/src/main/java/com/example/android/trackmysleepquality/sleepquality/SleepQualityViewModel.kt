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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*


class SleepQualityViewModel(
    private val sleepNightKey: Long = 0L, // Key from the navigation
    val database: SleepDatabaseDao // database from the factory
) : ViewModel() {

    // Creating own scope is no longer recommended, use viewModelScope instead
//
//        private val viewModelJob = Job()
//
//        private val uiScope = CoroutineScope { Dispatchers.Main + viewModelJob }

    // Navigation event
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigation() {
        _navigateToSleepTracker.value = null
    }

    // Icon click handler
    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {

                val tonight = database.get(sleepNightKey) ?: return@launch

                tonight.sleepQuality = quality

                database.update(tonight)
            // trigger navigation after setting sleep quality
            _navigateToSleepTracker.value = true
        }
    }
}
