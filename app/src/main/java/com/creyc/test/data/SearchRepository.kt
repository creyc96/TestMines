package com.creyc.test.data


import io.reactivex.Observable


class SearchRepository (private val apiService: BashImApiService){

    fun askAccess(id: String, locale: String): Observable<Link> {
        return apiService.askAccess(Parametrs( id, locale))
    }
}