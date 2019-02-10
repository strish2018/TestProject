package com.strish.android.testproject.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.strish.android.testproject.Article

@Dao
interface ArticleDao {

    @get:Query("SELECT * FROM article_table")
    val allArticles: LiveData<List<Article>>

    @Insert
    fun insert(article: Article)

    @Query("DELETE FROM article_table WHERE title = :title")
    fun deleteByTitle(title: String)
}
