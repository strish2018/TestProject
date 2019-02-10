package com.strish.android.testproject

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

import com.strish.android.testproject.database.ArticleDao
import com.strish.android.testproject.database.ArticleDatabase

class ArticleRepository(application: Application) {
    private val articleDao: ArticleDao
    val allArticles: LiveData<List<Article>>

    init {
        val database = ArticleDatabase.getInstance(application)
        articleDao = database.articleDao()
        allArticles = articleDao.allArticles
    }

    fun insert(article: Article) {
        InsertArticleAsyncTask(articleDao).execute(article)
    }

    fun deleteByTitle(title: String) {
        DeleteByTitleAsyncTask(articleDao).execute(title)
    }

    private class InsertArticleAsyncTask internal constructor(private val articleDao: ArticleDao) : AsyncTask<Article, Void, Void>() {

        override fun doInBackground(vararg articles: Article): Void? {
            articleDao.insert(articles[0])
            return null
        }
    }

    private class DeleteByTitleAsyncTask internal constructor(private val articleDao: ArticleDao) : AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg strings: String): Void? {
            articleDao.deleteByTitle(strings[0])
            return null
        }
    }

}
