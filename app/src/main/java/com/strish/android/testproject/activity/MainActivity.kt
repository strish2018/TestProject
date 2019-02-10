package com.strish.android.testproject.activity

import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import com.strish.android.testproject.fragments.AllArticlesFragment
import com.strish.android.testproject.ArticleViewModel
import com.strish.android.testproject.fragments.FavoritesFragment
import com.strish.android.testproject.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    var mArticleViewModel: ArticleViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mArticleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            if(position == 0){
                return AllArticlesFragment().newInstance()
            } else{
                return  FavoritesFragment().newInstance()
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

}
