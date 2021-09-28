package com.calendar.freeslots.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calendar.freeslots.FreeSlotsActivity
import com.calendar.freeslots.R
import com.calendar.freeslots.model.entity.Schedule
import com.calendar.freeslots.model.entity.ScheduleData
import com.calendar.freeslots.model.entity.Slot
import com.calendar.freeslots.model.entity.Title
import com.calendar.freeslots.model.exception.ViewModelNotSubscribedException
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

class SlotsRepository: ISlotsRepository {
    @Inject
    lateinit var gson: Gson
    @Inject
    lateinit var context: Context
    private val viewModelsSet: LinkedHashSet<ViewModel>

    init {
        FreeSlotsActivity.modelComponent.inject(this)
        viewModelsSet = LinkedHashSet()
    }

    override fun loadFreeSlots(
        fromIndex: Int,
        toIndex: Int,
        filterDuration: Int?
    ): Flow<List<ScheduleData>> = getScheduleFlow()
            .map { it.getScheduleArray(fromIndex, toIndex, filterDuration) }
            .flowOn(Dispatchers.IO)

    override fun subscribe(viewModel: ViewModel) {
        viewModelsSet.add(viewModel)
    }

    override fun unsubscribe(viewModel: ViewModel) {
        viewModelsSet.remove(viewModel)
    }

    override fun getCurrentCoroutineScope(): CoroutineScope {
        if (viewModelsSet.isEmpty()) throw ViewModelNotSubscribedException()
        return viewModelsSet.last().viewModelScope
    }

    private fun getScheduleFlow(): Flow<Schedule> = flow {
        emit(gson.fromJson(
            getJsonDataFromAsset(
                context,
                SCHEDULE_TEACHER_JSON
            ), Schedule::class.java
        ) as Schedule)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String) =
        try { context.assets.open(fileName).bufferedReader().use { it.readText() } }
        catch (ioException: IOException) { null }

    private fun Schedule.getScheduleArray(fromIndex: Int, toIndex: Int, durationSeconds: Int? = null): List<ScheduleData> {
        val todayIndex = getTodayIndex()
        val resultArray = ArrayList<ScheduleData>()
            .addDataIfActualDate(Title(0, R.string.sn, todayIndex), sn.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
            .addDataIfActualDate(Title(1, R.string.mn, todayIndex), mn.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
            .addDataIfActualDate(Title(2, R.string.tu, todayIndex), tu.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
            .addDataIfActualDate(Title(3, R.string.wd, todayIndex), wd.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
            .addDataIfActualDate(Title(4, R.string.th, todayIndex), th.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
            .addDataIfActualDate(Title(5, R.string.fr, todayIndex), fr.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
            .addDataIfActualDate(Title(6, R.string.st, todayIndex), st.getScheduleArraySortedAndFilteredByDuration(durationSeconds))
        return resultArray.subList(
            if (fromIndex >= toIndex || fromIndex < 0) 0 else fromIndex,
            if (toIndex < resultArray.lastIndex) toIndex else resultArray.lastIndex
        )
    }

    private fun ArrayList<Slot>.getScheduleArraySortedAndFilteredByDuration(durationSeconds: Int?): ArrayList<Slot> {
        sortBy { it.start}
        if (durationSeconds == null) return this
        val filteredArray = ArrayList<Slot>()
        val tempArray = ArrayList<Slot>()
        var tempArrayTotalTime = 0
        forEach {
            val slotTime = it.finish - it.start
            if (slotTime >= durationSeconds) {
                filteredArray.add(it)
            } else if (tempArray.isEmpty() || tempArray[tempArray.lastIndex].finish == it.start) {
                tempArray.add(it)
                tempArrayTotalTime += slotTime
            } else {
                if (tempArrayTotalTime >= durationSeconds) {
                    filteredArray.addAll(tempArray)
                }
                tempArray.clear()
                tempArrayTotalTime = 0
            }
        }
        return filteredArray
    }

    private fun ArrayList<ScheduleData>.addDataIfActualDate(title: Title, slots: ArrayList<Slot>): ArrayList<ScheduleData> {
        when {
            title.index > title.todayIndex -> {
                this.add(title)
                this.addAll(slots)
            }
            title.index == title.todayIndex -> {
                this.add(title)
                var startPosition = slots.lastIndex
                val secondsFromMidnight = getSecondsFromMidnight()
                run slotsLoop@ {
                    slots.forEachIndexed { index, slot ->
                        if (slot.start >= secondsFromMidnight) {
                            startPosition = index
                            return@slotsLoop
                        }
                    }
                }
                this.addAll(slots.subList(startPosition, slots.lastIndex))
            }
            else -> {
//                if need to show next week's schedule - save the data to a new array,
//                and then add it to the end of this collection
            }
        }
        return this
    }

    private fun getSecondsFromMidnight(): Int {
        val c = Calendar.getInstance()
        val now = c.timeInMillis
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0
        val passed = now - c.timeInMillis
        return (passed / 1000).toInt()
    }

    private fun getTodayIndex(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.DAY_OF_WEEK] - 1
    }

    companion object {
        private const val SCHEDULE_TEACHER_JSON = "schedule_teacher.json"
    }
}