package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Constants for RecyclerView to keep track of each type of item each view holder is holding
private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

// Goal: Take a list of sleep nights, adapt it into something that the RecyclerView can use to display on the screen
class SleepNightAdapter(val clickListener: SleepNightListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    // define a scope for converting list of sleep nights to data items
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    // How to draw an item
    // this method is only called for items that are either on screen, or just about to scroll onto the screen
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        // unwrap the Data item
        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }

    // return the right header or item constant depending on the type
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    // convert a List<SleepNight> to a List<DataItem>
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map {
                    DataItem.SleepNightItem(it)
                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    // How to create a new ViewHolder
    // Give Recyclerview a new ViewHolder whenever it asks
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> ViewHolder.TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
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

        class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
            companion object {
                fun from(parent: ViewGroup): TextViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val view = layoutInflater.inflate(R.layout.header, parent, false)
                    return TextViewHolder(view)
                }
            }
        }
    }
}

// Optimize changes to the data
class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    // Used to discover if an item was edit, removed, or moved
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        // it's important that you only check the IDS in this callback
        //  if they do, they represent the same thing
        return oldItem.id == newItem.id
    }

    // to determine if an item has changed
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

// Listen for clicks and pass related data for processing
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {

    // When user selects an item, this will be triggered with the selected item
    fun onClick(night: SleepNight) = clickListener(night.nightId)

}

// closed type: all subclasses must be define in this file
sealed class DataItem {
    // wrapper around SleepNight
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightId
    }

    // declare as object since it doesn't have any data
    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long

}