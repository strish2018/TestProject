package com.strish.android.testproject.utils

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.bind(compositeSubscription: CompositeDisposable): Disposable {
    compositeSubscription.add(this)
    return this
}