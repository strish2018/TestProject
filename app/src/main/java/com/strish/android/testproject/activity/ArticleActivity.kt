package com.strish.android.testproject.activity

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso
import com.strish.android.testproject.Article
import com.strish.android.testproject.R
import kotlinx.android.synthetic.main.article_layout.*

import maes.tech.intentanim.CustomIntent

class ArticleActivity : AppCompatActivity() {
    private val isNetworkAvailableAndConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isNetworkAvailable = cm.activeNetworkInfo != null
            return isNetworkAvailable && cm.activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_layout)
        //U only need a local var
        val article = intent.getSerializableExtra(ARGS_ARTICLE) as Article

        item_title_text_view.text = article.title
        item_byline_text_view.text = article.author
        item_abstract_text_view.text = article.content
        item_date_text_view.text = article.publishedAt?.substring(0, 10)
        if (isNetworkAvailableAndConnected) {
            Picasso.get().load(article.urlToImage).fit().centerCrop().into(image_view_item)
        } else {
            val data = article.thumbnailByte
            val bmp = BitmapFactory.decodeByteArray(data, 0, data?.size ?: 0)
            image_view_item.setImageBitmap(bmp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    companion object {
        const val ARGS_ARTICLE = "article"
    }
}
