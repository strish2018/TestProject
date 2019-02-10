package com.strish.android.testproject

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "article_table")
data class Article(var title: String) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var author: String? = null
    var description: String? = null
    var urlToImage: String? = null
    var url: String? = null
    var publishedAt: String? = null
    var content: String? = null
    var favorite: Boolean = false
    var thumbnailByte: ByteArray? = null
}

data class JSONResponse(
        val articles: List<Article>
)


