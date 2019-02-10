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

import maes.tech.intentanim.CustomIntent

class ArticleActivity : AppCompatActivity() {

    private var mArticle: Article? = null
    private var mImageView: ImageView? = null
    private var mTitleTextView: TextView? = null
    private var mBylineTextView: TextView? = null
    private var mAbstractTextView: TextView? = null
    private var mDateTextView: TextView? = null

    private val isNetworkAvailableAndConnected: Boolean
        get() {
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isNetworkAvailable = cm.activeNetworkInfo != null
            return isNetworkAvailable && cm.activeNetworkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_layout)
        mImageView = findViewById(R.id.image_view_item)
        mTitleTextView = findViewById(R.id.item_title_text_view)
        mBylineTextView = findViewById(R.id.item_byline_text_view)
        mAbstractTextView = findViewById(R.id.item_abstract_text_view)
        mDateTextView = findViewById(R.id.item_date_text_view)

        mArticle = intent.getSerializableExtra(ARGS_ARTICLE) as Article

        mTitleTextView!!.text = mArticle!!.title
        mBylineTextView!!.text = mArticle!!.author
        mAbstractTextView!!.text = mArticle!!.content
        mDateTextView!!.text = mArticle!!.publishedAt!!.substring(0, 10)
        if (isNetworkAvailableAndConnected) {
            Picasso.get().load(mArticle!!.urlToImage).fit().centerCrop().into(mImageView)
        } else {
            val data = mArticle!!.thumbnailByte
            val bmp = BitmapFactory.decodeByteArray(data, 0, data!!.size)
            mImageView!!.setImageBitmap(bmp)
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

        val ARGS_ARTICLE = "article"
    }
}
