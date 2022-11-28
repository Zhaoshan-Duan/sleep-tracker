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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Room Database
//  entities is all the tables version is set to 1,
//  if schema is true and schema is changed, have to up the version number
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    // Associate DAO
    abstract val sleepDatabaseDao: SleepDatabaseDao // one table

    // allow access without instantiating the class
    companion object {

        @Volatile // Never cached; All writes and read will be done to from the main memory
        // changes made by one thread to INSTANCE are visible to all other threads
        private var INSTANCE: SleepDatabase? = null // reference to the database

        // return a reference of the database
        fun getInstance(context: Context): SleepDatabase {
            synchronized(this) { // only one thread of execution at a time can enter this block

                // copy the current value of INSTANCE to a local variable (take advantage of smart cast to make sure we always return a sleep database )
                var instance = INSTANCE

                // check if there's already a database
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    // assign to the newly created database
                    INSTANCE = instance
                }

                return instance
            }
        }
    }

}