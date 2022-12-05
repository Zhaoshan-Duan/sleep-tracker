package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

// Goal: Take a list of sleep nights, adapt it into something that the RecyclerView can use to display on the screen
class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {


    // How to draw an item
    // this method is only called for items that are either on screen, or just about to scroll onto the screen
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item, clickListener)
    }


    // How to create a new ViewHolder
    // Give Recyclerview a new ViewHolder whenever it asks
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // Implementation detail of the RecyclerView
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // move how to update the view into the viewhold which has the views
        // update the method to use data binding
        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            // bind the sl,eep variable and item
            binding.sleep = item

            binding.clickListener = clickListener

            // Let data binding execute pending bindings right away
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                // create layout inflater based on the parent view
                val layoutInflater = LayoutInflater.from(parent.context)

                // binding object
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

                // return the view holder
                return ViewHolder(binding)
            }
        }
    }
}

// Optimize changes to the data
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    // Used to discover if an item was edit, removed, or moved
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        // it's important that you only check the IDS in this callback
        //  if they do, they represent the same thing
        return oldItem.nightId == newItem.nightId
    }

    // to determine if an item has changed
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}

// Listen for clicks and pass related data for processing
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {

    // When user selects an item, this will be triggered with the selected item
    fun onClick(night: SleepNight) = clickListener(night.nightId)


}