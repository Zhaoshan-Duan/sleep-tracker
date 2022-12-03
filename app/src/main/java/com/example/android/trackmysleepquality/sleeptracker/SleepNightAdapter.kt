package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.*
import com.example.android.trackmysleepquality.database.SleepNight

// Goal: Take a list of sleep nights, adapt it into something that the RecyclerView can use to display on the screen
class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {

    // Holds a list of sleep night
    var data = listOf<SleepNight>()
        set(value) {
            field = value // set the value in a setter
            notifyDataSetChanged() // let RecyclerView immediately redraw everything
        }

    override fun getItemCount() = data.size

    // How to draw an item
    // this method is only called for items that are either on screen, or just about to scroll onto the screen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    // How to create a new ViewHolder
    // Give Recyclerview a new ViewHolder whenever it asks
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // get reference of the views that this ViewHolder will hold
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        // move how to update the view into the viewhold which has the views
        fun bind(item: SleepNight) {
            val res = itemView.context.resources

            sleepLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQuality, res)

            qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                // create layout inflater based on the parent view
                val layoutInflater = LayoutInflater.from(parent.context)

                // a reusable pattern
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)

                // return the view holder
                return ViewHolder(view)
            }
        }
    }


}