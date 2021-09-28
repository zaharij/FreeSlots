package com.calendar.freeslots.model.dinjection

import android.content.Context
import com.calendar.freeslots.FreeSlotsActivity
import com.calendar.freeslots.model.SlotsRepository
import com.google.gson.Gson
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(modules = [ModelModule::class])
interface ModelComponent {
    fun inject(slotsService: SlotsRepository)
}

@Module
object ModelModule {
    @Provides
    fun provideAppContext(): Context {
        return FreeSlotsActivity.getAppContext()
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}