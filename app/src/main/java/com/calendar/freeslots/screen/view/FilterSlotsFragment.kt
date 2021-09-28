package com.calendar.freeslots.screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.freeslots.R
import com.google.android.material.radiobutton.MaterialRadioButton

class FilterSlotsFragment : BaseScheduleFragment(), View.OnClickListener {

    private lateinit var filterHalfHourRadioButton: MaterialRadioButton
    private lateinit var filterOneHourRadioButton: MaterialRadioButton
    private lateinit var filterTwoHoursRadioButton: MaterialRadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_slots, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterHalfHourRadioButton = view.findViewById(R.id.filterHalfHourRadioButton)
        filterOneHourRadioButton = view.findViewById(R.id.filterOneHourRadioButton)
        filterTwoHoursRadioButton = view.findViewById(R.id.filterTwoHoursRadioButton)

        filterHalfHourRadioButton.setOnClickListener(this)
        filterOneHourRadioButton.setOnClickListener(this)
        filterTwoHoursRadioButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        freeSlotsViewModel.onFilterByDurationClicked(
            when (v?.id) {
                filterHalfHourRadioButton.id -> { HALF_HOUR_SEC }
                filterOneHourRadioButton.id -> { ONE_HOUR_SEC }
                filterTwoHoursRadioButton.id -> { TWO_HOUR_SEC }
                else -> { 0 }
            }
        )
    }

    companion object {
        private const val HALF_HOUR_SEC = 30 * 60
        private const val ONE_HOUR_SEC = HALF_HOUR_SEC * 2
        private const val TWO_HOUR_SEC = ONE_HOUR_SEC * 2
    }
}