package com.ptrv.news

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ptrv.common.di.ComponentDependenciesProvider
import com.ptrv.common.di.HasChildDependencies
import com.ptrv.news.di.AppComponent
import com.ptrv.news.di.DaggerAppComponent
import javax.inject.Inject

class App : Application(), HasChildDependencies {
    lateinit var appComponent: AppComponent
    @Inject
    override lateinit var dependencies: ComponentDependenciesProvider

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        appComponent = DaggerAppComponent.builder().context(this).build()
        appComponent.inject(this)
    }
}