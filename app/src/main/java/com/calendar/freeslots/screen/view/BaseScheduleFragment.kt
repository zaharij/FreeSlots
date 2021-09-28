package com.calendar.freeslots.screen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.calendar.freeslots.FreeSlotsActivity
import com.calendar.freeslots.screen.viewmodel.FreeSlotsViewModel

abstract class BaseScheduleFragment : Fragment() {
    protected lateinit var freeSlotsViewModel: FreeSlotsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        freeSlotsViewModel = (activity as FreeSlotsActivity).freeSlotsViewModel
    }
}