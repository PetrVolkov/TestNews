package com.ptrv.news.presentation

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.ptrv.news.domain.News

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : MvpView {
    fun showEmptyProgress(show: Boolean)
    fun showEmptyError(show: Boolean)
    fun showNews(show: Boolean, news: List<News>)
    fun showRefreshProgress(show: Boolean)
    fun showError(show: Boolean)
}