package com.praktica.bchbank.data

import com.creyc.test.data.BashImApiService
import com.creyc.test.data.SearchRepository

object SearchRepositoryProvider{
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(BashImApiService.create())
    }
}