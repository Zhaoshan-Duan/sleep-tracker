package org.jojo.sleep_tracker

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.jojo.sleep_tracker.database.SleepDatabase
import org.jojo.sleep_tracker.database.SleepDatabaseDao
import org.jojo.sleep_tracker.database.SleepNight
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {
    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase
    @Before
    fun createDB() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.
        inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        sleepDao = db.sleepDatabaseDao
    }


    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(tonight?.sleepQuality, -1)
    }
}