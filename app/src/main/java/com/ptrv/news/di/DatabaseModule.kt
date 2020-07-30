package com.ptrv.news.di

import android.arch.persistence.room.Room
import android.content.Context
import com.ptrv.news.data.db.NewsDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @JvmStatic
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        NewsDb::class.java,
        "news_database.db"
    ).build()
}