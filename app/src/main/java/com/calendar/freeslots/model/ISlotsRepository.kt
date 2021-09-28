package com.calendar.freeslots.model

import androidx.lifecycle.ViewModel
import com.calendar.freeslots.model.entity.ScheduleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ISlotsRepository {
    fun loadFreeSlots(fromIndex: Int, toIndex: Int, filterDuration: Int?): Flow<List<ScheduleData>>
    fun subscribe(viewModel: ViewModel)
    fun unsubscribe(viewModel: ViewModel)
    fun getCurrentCoroutineScope(): CoroutineScope
}