package com.example.android.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight

// Goal: Take a list of sleep nights, adapt it into something that the RecyclerView can use to display on the screen
class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>() {

    // Holds a list of sleep night
    var data = listOf<SleepNight>()
        set(value) {
            field = value // set the value in a setter
            notifyDataSetChanged() // let RecyclerView immediately redraw everything
        }

    override fun getItemCount() = data.size

    // How to draw an item
    // this method is only called for items that are either on screen, or just about to scroll onto the screen
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]

        if (item.sleepQuality <= 1) {
            holder.textView.setTextColor(Color.RED)
        } else {
            holder.textView.setTextColor(Color.BLACK)
        }

        holder.textView.text = item.sleepQuality.toString()
    }

    // How to create a new ViewHolder
    // Give Recyclerview a new ViewHolder whenever it asks
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {

        // create layout inflater based on the parent view
        val layoutInflater = LayoutInflater.from(parent.context)

        // a reusable pattern
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView

        // return the view holder
        return TextItemViewHolder(view)
    }


}