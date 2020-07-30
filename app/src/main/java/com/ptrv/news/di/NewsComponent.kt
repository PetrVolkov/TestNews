package com.ptrv.news.di

import com.ptrv.common.di.ComponentDependencies
import com.ptrv.common.di.DaggerComponent
import com.ptrv.common.di.PerFragment
import com.ptrv.common.di.Rx
import com.ptrv.news.data.db.NewsDb
import com.ptrv.news.data.network.NewsApi
import com.ptrv.news.data.repository.NewsRepositoryImpl
import com.ptrv.news.domain.NewsInteractor
import com.ptrv.news.domain.NewsInteractorImpl
import com.ptrv.news.domain.NewsRepository
import com.ptrv.news.presentation.NewsPresenter
import com.ptrv.news.ui.NewsFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import retrofit2.Retrofit
import ru.terrakok.cicerone.Router

@PerFragment
@Component(modules = [NewsModule::class], dependencies = [NewsDependencies::class])
interface NewsComponent : DaggerComponent {
    fun inject(newsFragment: NewsFragment)
}

@Module
abstract class NewsModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        @PerFragment
        fun provideNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

        @Provides
        @JvmStatic
        @PerFragment
        fun provideNewsPresenter(
            router: Router,
            newsInteractor: NewsInteractor,
            @Rx.MainThread mainThreadScheduler: Scheduler
        ) = NewsPresenter(newsInteractor, router, mainThreadScheduler)
    }

    @Binds
    @PerFragment
    abstract fun provideNewsRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @Binds
    @PerFragment
    abstract fun provideNewsInteractor(newsInteractorImpl: NewsInteractorImpl): NewsInteractor

}

interface NewsDependencies : ComponentDependencies {
    @Rx.MainThread
    fun mainThreadScheduler(): Scheduler
    @Rx.Io
    fun ioScheduler(): Scheduler
    fun router(): Router
    fun retrofit(): Retrofit
    fun database(): NewsDb
}
