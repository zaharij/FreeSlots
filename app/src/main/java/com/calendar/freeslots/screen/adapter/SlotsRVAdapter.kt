package com.calendar.freeslots.screen

import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.calendar.freeslots.R
import com.calendar.freeslots.model.entity.ScheduleData
import com.calendar.freeslots.model.entity.Slot
import com.calendar.freeslots.model.entity.Title
import com.calendar.freeslots.model.entity.slotDiffUtilCallback

class SlotsRVAdapter: PagedListAdapter<ScheduleData, SlotViewHolder>(slotDiffUtilCallback) {

    val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int) =
            if (getItem(position) is Slot) TITLE_COLUMNS_COUNT else LIST_COLUMNS_COUNT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SlotViewHolder(parent)

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val scheduleData = getItem(position)
        holder.bindTo(scheduleData)
    }

    companion object {
        const val LIST_COLUMNS_COUNT = 4
        const val TITLE_COLUMNS_COUNT = 1
    }
}

class SlotViewHolder (private val parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)) {

    private val startTextView = itemView.findViewById<TextView>(R.id.startTextView)
    private val finishTextView = itemView.findViewById<TextView>(R.id.finishTextView)
    private val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
    private val divTextView = itemView.findViewById<TextView>(R.id.divTextView)
    private lateinit var scheduleData: ScheduleData

    fun bindTo(scheduleData: ScheduleData?) {
        if (scheduleData == null) return
        this.scheduleData = scheduleData
        if (scheduleData is Slot) {
            prepareView(true)
            startTextView.text = scheduleData.getStartString()
            finishTextView.text = scheduleData.getFinishString()
        } else {
            prepareView(false)
            titleTextView.text = (scheduleData as Title).getTitle(parent.context)
        }
    }

    private fun prepareView(isSlot: Boolean) {
        titleTextView.visibility = if (isSlot) INVISIBLE else VISIBLE
        startTextView.visibility = if (isSlot) VISIBLE else INVISIBLE
        finishTextView.visibility = if (isSlot) VISIBLE else INVISIBLE
        divTextView.visibility = if (isSlot) VISIBLE else INVISIBLE
    }
}