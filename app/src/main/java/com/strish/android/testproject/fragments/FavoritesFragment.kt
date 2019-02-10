package com.strish.android.testproject.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.strish.android.testproject.Article
import com.strish.android.testproject.ArticleViewModel
import com.strish.android.testproject.R
import com.strish.android.testproject.activity.ArticleActivity
import com.strish.android.testproject.activity.MainActivity
import com.strish.android.testproject.adapters.FavoritesAdapter
import maes.tech.intentanim.CustomIntent

class FavoritesFragment : Fragment(), FavoritesAdapter.OnItemClickedListener {

    private var mArticleViewModel: ArticleViewModel? = null
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mAdapter: FavoritesAdapter? = null

    fun newInstance(): FavoritesFragment {
        val fragment = FavoritesFragment()
        return fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.articles_fragment_layout, container, false)

        mArticleViewModel = (activity as MainActivity).mArticleViewModel

        mRecyclerView = v.findViewById(R.id.recycler_view)
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView?.layoutManager = mLayoutManager
        mAdapter = FavoritesAdapter()
        mRecyclerView?.adapter = mAdapter
        mAdapter?.setOnItemClickedListener(this)

        mArticleViewModel?.getFavoriteArticles()?.observe(this, Observer<List<Article>> { articles ->
            mAdapter?.submitList(articles)
        })

        return v
    }

    override fun onItemClicked(article: Article?) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra(ArticleActivity.ARGS_ARTICLE, article)
        startActivity(intent)
        CustomIntent.customType(activity, "fadein-to-fadeout")
    }

    override fun onFavoriteButtonClicked(article: Article?) {
        article?.favorite = true
        mArticleViewModel?.favoriteButtonClicked(article)
    }

    override fun onShareClicked(article: Article?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check this out: " + article?.url)
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }
}