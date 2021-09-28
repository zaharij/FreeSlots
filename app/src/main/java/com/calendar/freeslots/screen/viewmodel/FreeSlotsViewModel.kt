package com.calendar.freeslots.screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.calendar.freeslots.FreeSlotsActivity
import com.calendar.freeslots.model.ISlotsRepository
import com.calendar.freeslots.model.entity.ScheduleData
import com.calendar.freeslots.screen.datasource.SlotsPositionalDataSource
import com.calendar.freeslots.screen.datasource.SlotsPositionalDataSourceFactory
import java.util.concurrent.Executors
import javax.inject.Inject

class FreeSlotsViewModel @Inject constructor(private val slotsRepository: ISlotsRepository): ViewModel() {

    private val mutableSlotsDataSourceLiveData: LiveData<SlotsPositionalDataSource>
    val slotsPageList: LiveData<PagedList<ScheduleData>>
    private val factory: SlotsPositionalDataSourceFactory

    init {
        slotsRepository.subscribe(this)
        FreeSlotsActivity.appComponent.inject(this)
         factory = SlotsPositionalDataSourceFactory(slotsRepository)
        mutableSlotsDataSourceLiveData = factory.getMutableLiveData()
        val pageListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(1)
            .setPageSize(48)
            .build()
        slotsPageList = LivePagedListBuilder(factory, pageListConfig)
            .setFetchExecutor(Executors.newFixedThreadPool(5))
            .build()

    }

    override fun onCleared() {
        super.onCleared()
        slotsRepository.unsubscribe(this)
    }

    fun onFilterByDurationClicked(duration: Int) {
        factory.filterDuration = duration
        mutableSlotsDataSourceLiveData.value?.invalidate()
    }
}