package com.calendar.freeslots.screen.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.calendar.freeslots.model.ISlotsRepository
import com.calendar.freeslots.model.entity.ScheduleData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SlotsPositionalDataSource(
    private val slotsStorage: ISlotsRepository,
    private val filterDuration: Int? = null
) : PositionalDataSource<ScheduleData>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<ScheduleData>) {
        slotsStorage.getCurrentCoroutineScope().launch {
            slotsStorage.loadFreeSlots(
                params.requestedStartPosition,
                params.requestedLoadSize,
                filterDuration
            ).collect {
                callback.onResult(it, 0)
            }
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ScheduleData>) {
        slotsStorage.getCurrentCoroutineScope().launch {
            slotsStorage.loadFreeSlots(
                params.startPosition,
                params.loadSize + params.startPosition,
                filterDuration
            ).collect {
                callback.onResult(it)
            }
        }
    }
}

class SlotsPositionalDataSourceFactory (
    private val slotsStorage: ISlotsRepository,
    private val mutableLiveData: MutableLiveData<SlotsPositionalDataSource> = MutableLiveData()
): DataSource.Factory<Int, ScheduleData>() {
    var filterDuration: Int? = null

    override fun create(): SlotsPositionalDataSource {
        val slotsPositionalDataSource = SlotsPositionalDataSource(slotsStorage, filterDuration)
        mutableLiveData.postValue(slotsPositionalDataSource)
        return slotsPositionalDataSource
    }

    fun getMutableLiveData() = mutableLiveData
}