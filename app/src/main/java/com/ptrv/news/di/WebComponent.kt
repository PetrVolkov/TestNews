package com.ptrv.news.di

import com.ptrv.common.di.ComponentDependencies
import com.ptrv.common.di.DaggerComponent
import com.ptrv.news.ui.WebFragment
import dagger.Component
import ru.terrakok.cicerone.Router

@Component(dependencies = [WebDeps::class])
interface WebComponent : DaggerComponent {
    fun inject(webFragment: WebFragment)
}

interface WebDeps : ComponentDependencies {
    fun router(): Router
}