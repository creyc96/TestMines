package com.creyc.test.presenters


import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.creyc.test.data.SearchRepository
import com.creyc.test.views.SplashView
import com.praktica.bchbank.data.SearchRepositoryProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

@InjectViewState
class SplashPresenter : MvpPresenter<SplashView>() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val repository: SearchRepository = SearchRepositoryProvider.provideSearchRepository()
    var ans = ""

    fun requestServer(id: String, locale: String){
        viewState.startChecking("Загрузка")
        compositeDisposable.add(
            repository.askAccess(id, locale)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    viewState.endChecking()
                    if (ans.contains("https://"))
                        viewState.changeFragmentToWebView(ans)
                    else
                        viewState.changeFragmentToMinesweeper()
                }
                .subscribe (
                    { result ->
                        ans = result.url
                    },
                    { error ->
                        Log.e("qwerty", error.toString())
                        viewState.toastMe(error.toString())
                        viewState.endChecking()
                    }
                )
        )

    }
}