package com.creyc.test.data




import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface BashImApiService {

    @POST("test.php")
    fun askAccess(
        @Body loginRequest: Parametrs
    ): Observable<Link>


    companion object Factory {
        fun create(): BashImApiService {
            val gson: Gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("http://109.120.149.72/")
                .build()
            return retrofit.create(BashImApiService::class.java)
        }
    }
}