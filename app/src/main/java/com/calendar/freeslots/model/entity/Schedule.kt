package com.calendar.freeslots.model.entity

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.calendar.freeslots.R
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Schedule (
    @SerializedName("0") val sn: ArrayList<Slot>,
    @SerializedName("1") val mn: ArrayList<Slot>,
    @SerializedName("2") val tu: ArrayList<Slot>,
    @SerializedName("3") val wd: ArrayList<Slot>,
    @SerializedName("4") val th: ArrayList<Slot>,
    @SerializedName("5") val fr: ArrayList<Slot>,
    @SerializedName("6") val st: ArrayList<Slot>
    )

abstract class ScheduleData

data class Slot (
    @SerializedName("start") val start: Int,
    @SerializedName("finish") val finish: Int
) : ScheduleData() {

    fun getStartString(): String {
        return start.convertToHMmToString()
    }

    fun getFinishString(): String {
        return finish.convertToHMmToString()
    }

    private fun Int.convertToHMmToString(): String {
        val m = this / 60 % 60
        val h = this / (60 * 60) % 24
        return String.format("%d:%02d", h, m)
    }
}

data class Title(val index: Int, private val dayRes: Int, val todayIndex: Int) : ScheduleData() {

    fun getTitle(context: Context): String {
        return context.getString(dayRes,
            if (index == todayIndex) getDateString() else
                getCalculatedDate(DATE_FORMAT, index - todayIndex)
        )
    }

    private fun getDateString(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT)
        val d = Date()
        return ", ${sdf.format(d)}"
    }

    private fun getCalculatedDate(dateFormat: String, days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(dateFormat)
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    companion object {
        private const val DATE_FORMAT = "d MMM"
    }
}

val slotDiffUtilCallback = object: DiffUtil.ItemCallback<ScheduleData>() {
    override fun areItemsTheSame(oldItem: ScheduleData, newItem: ScheduleData) = oldItem.isEquals(newItem)
    override fun areContentsTheSame(oldItem: ScheduleData, newItem: ScheduleData) = oldItem.isEquals(newItem)
    private fun ScheduleData.isEquals(newItem: ScheduleData) =
        if (this is Slot && newItem is Slot) this == newItem else (this is Title) == (newItem is Title)
}