package com.strish.android.testproject

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.strish.android.testproject.api.NewsApiService
import com.strish.android.testproject.utils.bind
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeDisposable = CompositeDisposable()

    var resetAdapter: Boolean = false
    private var sortByPopularity: Boolean = true
    private val repository: ArticleRepository = ArticleRepository(application)
    var favoriteArticles: LiveData<List<Article>>? = null
    var openActivityLiveData: MutableLiveData<Article>? = MutableLiveData()

    var dateFrom: Date = Date()
        private set

    var dateTo: Date = Date()
        private set

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    var articles: MutableLiveData<List<Article>>? = MutableLiveData()
        internal set

    private var mlistener: OnListUpdatedListener? = null

    interface OnListUpdatedListener {
        fun onListUpdated(articles: List<Article>)
    }

    fun setOnListUpdatedListener(listener: OnListUpdatedListener) {
        mlistener = listener
    }

    init {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -15)
        dateFrom = cal.time
        favoriteArticles = repository.allArticles
        fetchNewArticles(1)
    }

    fun listScrolled(pageNum: Int) {
        fetchNewArticles(pageNum)
    }

    private fun fetchNewArticles(pageNum: Int) {
        val apiService = NewsApiService.create()
        val sortBy: String = if (sortByPopularity) {
            "popularity"
        } else {
            "publishedAt"
        }

        apiService.newArticles(pageNum, sortBy, sdf.format(dateFrom), sdf.format(dateTo))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    checkArticles(result.articles)
                    if (pageNum != 1) {
                        resetAdapter = false
                        val list = mutableListOf<Article>()
                        list.addAll(articles?.value!!.asIterable())
                        list.addAll(result.articles)
                        articles?.postValue(list)
                    } else {
                        resetAdapter = true
                        articles?.postValue(result.articles)
                    }
                }, { error ->
                    error.printStackTrace()
                    val list: List<Article> = emptyList()
                    articles?.postValue(list)
                }).bind(compositeDisposable)
    }

    private fun checkArticles(articles: List<Article>) {
        val favorites: List<Article> = repository.allArticles.value!!
        for (art in articles) {
            for (fav in favorites) {
                //equals can be replaced with == in Kotlin
                if (art.title == fav.title) {
                    art.favorite = true
                }
            }
        }
    }

    fun articleClicked(article: Article?) {
        openActivityLiveData?.value = article
    }

    fun sortByPopularityClicked() {
        sortByPopularity = true
        fetchNewArticles(1)
    }

    fun sortByTimeClicked() {
        sortByPopularity = false
        fetchNewArticles(1)
    }

    fun fromDateSet(d: Date) {
        dateFrom = d
        fetchNewArticles(1)
    }

    fun toDateSet(d: Date) {
        dateTo = d
        fetchNewArticles(1)
    }

    fun favoriteButtonClicked(article: Article?) {
        //With this check we can avoid a lot of null-checking
        if (article == null) return

        if (article.favorite) {
            article.favorite = false
            deleteByTitle(article.title)


//            for (art in articles?.value!!.asIterable()) {
//                if (art.title == article.title) {
//                    art.favorite = false
//                }
//            }
//            This could be replaced by the next one line
//            And btw, u should not search by title 'cause they can repeat sometimes
//            Better search and delete by id

            articles?.value?.first { it.title == article.title }?.favorite = false

//            Be careful with not-null assertion operator. Better do like this
            articles?.value?.run { mlistener?.onListUpdated(this) }
        } else {
            article.favorite = true
            saveArticle(article)
        }
    }

    private fun saveArticle(article: Article) {
        repository.insert(article)
    }

    private fun deleteByTitle(title: String) {
        repository.deleteByTitle(title)
    }

    fun onDestroy(){
        //Use composite disposable if u have any rx calls
        compositeDisposable.dispose()
    }
}
