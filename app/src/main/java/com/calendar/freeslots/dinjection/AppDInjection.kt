package com.calendar.freeslots.dinjection

import com.calendar.freeslots.FreeSlotsActivity
import com.calendar.freeslots.model.ISlotsRepository
import com.calendar.freeslots.model.SlotsRepository
import com.calendar.freeslots.screen.view.BaseScheduleFragment
import com.calendar.freeslots.screen.viewmodel.FreeSlotsViewModel
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(freeSlotsViewModel: FreeSlotsViewModel)
    fun inject(baseScheduleFragment: BaseScheduleFragment)
    fun inject(freeSlotsActivity: FreeSlotsActivity)
}

@Module
object AppModule {
    @Provides
    fun provideIDataManager(): ISlotsRepository {
        return SlotsRepository()
    }

    @Provides
    fun provideFreeSlotsViewModel(slotsRepository: ISlotsRepository): FreeSlotsViewModel {
        return FreeSlotsViewModel(slotsRepository)
    }
}