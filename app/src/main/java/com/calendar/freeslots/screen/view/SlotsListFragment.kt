package com.calendar.freeslots.screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.calendar.freeslots.R
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.calendar.freeslots.model.entity.ScheduleData
import com.calendar.freeslots.screen.SlotsRVAdapter
import com.calendar.freeslots.screen.SlotsRVAdapter.Companion.LIST_COLUMNS_COUNT


class SlotsListFragment : BaseScheduleFragment() {

    private lateinit var slots: PagedList<ScheduleData>
    private lateinit var slotsListRecyclerView: RecyclerView
    private lateinit var slotsAdapter: SlotsRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_slots_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slotsListRecyclerView = view.findViewById(R.id.slotsListRecyclerView)
        slotsAdapter = SlotsRVAdapter()
        freeSlotsViewModel.slotsPageList.observe(viewLifecycleOwner) {
            slots = it
            showOnRecyclerView()
        }
    }

    private fun showOnRecyclerView() {
        slotsAdapter.submitList(slots)
        slotsListRecyclerView.layoutManager =
            GridLayoutManager(context, LIST_COLUMNS_COUNT).also {
            it.spanSizeLookup = slotsAdapter.spanSizeLookup
        }
        slotsListRecyclerView.adapter = slotsAdapter
        slotsAdapter.notifyDataSetChanged()
    }
}