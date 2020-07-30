package com.ptrv.news.ui

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.ptrv.common.di.findComponentDependencies
import com.ptrv.common.ui.BaseFragment
import com.ptrv.common.ui.visible
import com.ptrv.news.R
import com.ptrv.news.di.DaggerNewsComponent
import com.ptrv.news.di.NewsComponent
import com.ptrv.news.domain.News
import com.ptrv.news.presentation.NewsPresenter
import com.ptrv.news.presentation.NewsView
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.layout_error.*
import javax.inject.Inject

class NewsFragment : BaseFragment(), NewsView {
    override val layoutRes: Int = R.layout.fragment_news
    private val viewHandler = Handler()

    init {
        componentBuilder = {
            DaggerNewsComponent.builder().newsDependencies(findComponentDependencies()).build()
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: NewsPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private val adapter: NewsAdapter by lazy {
        NewsAdapter(
            { presenter.onNewsClick(it) },
            { presenter.loadNextNewsPage() },
            { presenter.loadNextNewsPage() }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getComponent<NewsComponent>().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@NewsFragment.adapter
            itemAnimator = DefaultItemAnimator()
        }
        swipeRefreshLayout.setOnRefreshListener { presenter.refreshNews() }
        retryButton.setOnClickListener { presenter.refreshNews() }
    }

    override fun showEmptyProgress(show: Boolean) {
        progressView.visible(show)
        swipeRefreshLayout.visible(!show)
        viewHandler.post { swipeRefreshLayout.isRefreshing = false }
    }

    override fun showNews(show: Boolean, news: List<News>) {
        recyclerView.visible(show)
        viewHandler.post { adapter.setData(news) }
    }

    override fun showRefreshProgress(show: Boolean) {
        viewHandler.post { swipeRefreshLayout.isRefreshing = show }
    }

    override fun showError(show: Boolean) {
        viewHandler.post { adapter.showNetworkError(show) }
    }

    override fun showEmptyError(show: Boolean) {
        errorView.visible(show)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
