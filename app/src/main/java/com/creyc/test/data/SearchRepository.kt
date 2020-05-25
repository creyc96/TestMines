package com.creyc.test.data


import android.telecom.Call
import io.reactivex.Observable
import retrofit2.http.Url


class SearchRepository (private val apiService: BashImApiService){

    fun askAccess(id: String, locale: String): Observable<Link> {
        return apiService.askAccess(Parametrs( id, locale))
    }
}