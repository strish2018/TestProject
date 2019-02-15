package com.strish.android.testproject.activity

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.strish.android.testproject.Article
import com.strish.android.testproject.ArticleViewModel
import com.strish.android.testproject.R
import com.strish.android.testproject.fragments.AllArticlesFragment
import com.strish.android.testproject.fragments.FavoritesFragment
import kotlinx.android.synthetic.main.activity_main.*
import maes.tech.intentanim.CustomIntent

class MainActivity : AppCompatActivity() {
    /*
     * Better use synthetic instead of findViewById
     */

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    var mArticleViewModel: ArticleViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mArticleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)
        mArticleViewModel?.openActivityLiveData = MutableLiveData()
        mArticleViewModel?.openActivityLiveData?.observe(this, Observer<Article> { article ->
            openArticleActivity(article)
        })

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                AllArticlesFragment().newInstance()
            } else {
                FavoritesFragment().newInstance()
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    private fun openArticleActivity(article: Article?) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(ArticleActivity.ARGS_ARTICLE, article)
        startActivity(intent)
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    override fun onDestroy() {
        super.onDestroy()
        mArticleViewModel?.onDestroy()
    }
}
