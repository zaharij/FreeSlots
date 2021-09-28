package com.calendar.freeslots

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.calendar.freeslots.dinjection.AppComponent
import com.calendar.freeslots.dinjection.DaggerAppComponent
import com.calendar.freeslots.model.dinjection.DaggerModelComponent
import com.calendar.freeslots.model.dinjection.ModelComponent
import com.calendar.freeslots.screen.view.FilterSlotsFragment
import com.calendar.freeslots.screen.view.SlotsListFragment
import com.calendar.freeslots.screen.viewmodel.FreeSlotsViewModel
import javax.inject.Inject


class FreeSlotsActivity : AppCompatActivity() {

    @Inject
    lateinit var freeSlotsViewModel: FreeSlotsViewModel
    private val listFragment: SlotsListFragment by lazy { SlotsListFragment() }
    private val filterFragment: FilterSlotsFragment by lazy { FilterSlotsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appInstance = application
        appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        openFragment(filterFragment, R.id.filterFragmentContainer, "FilterSlotsFragment")
        openFragment(listFragment, R.id.slotsListFragmentContainer, "SlotsListFragment")
    }

    private fun openFragment(fragment: Fragment, fragmentContainerId: Int, tag: String) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentContainerId, fragment, tag)
        transaction.commit()
    }

    companion object {
        private lateinit var appInstance: Application
        fun getAppContext(): Context = appInstance.applicationContext
        val appComponent: AppComponent by lazy { DaggerAppComponent.create() }
        val modelComponent: ModelComponent by lazy { DaggerModelComponent.create() }
    }
}