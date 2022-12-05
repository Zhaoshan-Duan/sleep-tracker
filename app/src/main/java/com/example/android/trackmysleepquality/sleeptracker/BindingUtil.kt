package com.example.android.trackmysleepquality.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight


// Setting text on a TextView with attribute "sleepDurationFormatted"
@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) {
    // bind data to the view
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}

// Set the sleep quality based on the value in a SleepNight
@BindingAdapter("sleepQuality")
fun TextView.setSleepQuality(item: SleepNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}

// Set the image on the Image View
@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?) {
    item?.let {
        setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }
}
