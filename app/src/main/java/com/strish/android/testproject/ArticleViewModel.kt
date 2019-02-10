package com.strish.android.testproject

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.strish.android.testproject.api.NewsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    var resetAdapter: Boolean = false
    private var sortByPopularity: Boolean = true
    private val repository: ArticleRepository = ArticleRepository(application)
    private var favoriteArticles: LiveData<List<Article>>? = null

    var dateFrom: Date = Date()
        private set

    var dateTo: Date = Date()
        private set

    private val sdf = SimpleDateFormat("yyyy-MM-dd")

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
        val sortBy: String
        if (sortByPopularity) {
            sortBy = "popularity"
        } else {
            sortBy = "publishedAt"
        }
        Log.d("Test", "pageNum " + pageNum + "   from " + dateFrom.toString() + "    to " + dateTo.toString())
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
                })
    }

    private fun checkArticles(articles: List<Article>) {
        val favorites: List<Article> = repository.allArticles.value!!
        for (art in articles) {
            for (fav in favorites) {
                if (art.title.equals(fav.title)) {
                    art.favorite = true
                }
            }
        }
    }

    fun getFavoriteArticles(): LiveData<List<Article>> {
        return favoriteArticles!!
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
        if (article?.favorite == true) {
            article.favorite = false
            deleteByTitle(article.title)
            for (art in articles?.value!!.asIterable()) {
                if (art.title.equals(article.title)) {
                    art.favorite = false
                }
            }
            mlistener?.onListUpdated(articles?.value!!)
        } else {
            article?.favorite = true
            saveArticle(article!!)
        }
    }

    private fun saveArticle(article: Article) {
        repository.insert(article)
    }

    private fun deleteByTitle(title: String) {
        repository.deleteByTitle(title)
    }

}
