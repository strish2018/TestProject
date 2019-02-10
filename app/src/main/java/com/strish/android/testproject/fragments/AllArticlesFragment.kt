package com.strish.android.testproject.fragments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.strish.android.testproject.*
import com.strish.android.testproject.activity.ArticleActivity
import com.strish.android.testproject.activity.MainActivity
import com.strish.android.testproject.adapters.ArticlesAdapter
import maes.tech.intentanim.CustomIntent
import java.util.*

class AllArticlesFragment : Fragment(), DateDialog.DateDialogListener, ArticlesAdapter.OnItemClickedListener, ArticleViewModel.OnListUpdatedListener {

    private var mRecyclerView: RecyclerView? = null
    private var mEmptyTextView: TextView? = null
    private var mAdapter: ArticlesAdapter? = null
    private var mArticleViewModel: ArticleViewModel? = null
    private var mLayoutManager: LinearLayoutManager? = null


    fun newInstance(): AllArticlesFragment {
        val fragment = AllArticlesFragment()
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.articles_fragment_layout, container, false)

        mArticleViewModel = (activity as MainActivity).mArticleViewModel

        mRecyclerView = v.findViewById(R.id.recycler_view)
        mEmptyTextView = v.findViewById(R.id.empty_view)
        mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView?.layoutManager = mLayoutManager
        mAdapter = ArticlesAdapter(listOf())
        mRecyclerView?.adapter = mAdapter
        setScrollListener()
        mAdapter?.setOnItemClickedListener(this)

        mArticleViewModel?.setOnListUpdatedListener(this)
        mArticleViewModel?.articles?.observe(this, Observer<List<Article>> { articles ->

            if (articles?.isEmpty() == true) {
                mRecyclerView?.visibility = View.GONE
                mEmptyTextView?.visibility = View.VISIBLE
            } else {
                mRecyclerView?.visibility = View.VISIBLE
                mEmptyTextView?.visibility = View.GONE
            }

            if (mArticleViewModel?.resetAdapter == true) {
                removeScrollListener()
                mAdapter?.setArticles(articles!!)
                setScrollListener()
            } else {
                mAdapter?.setArticles(articles!!)
            }
        })

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_sortBy_popularity -> mArticleViewModel?.sortByPopularityClicked()
            R.id.action_sortBy_time -> mArticleViewModel?.sortByTimeClicked()
            R.id.action_filterByTime_from -> timeFilterFromClicked()
            R.id.action_filterByTime_to -> timeFilterToClicked()
        }
        return true
    }

    private fun setScrollListener() {
        mRecyclerView?.addOnScrollListener(object : EndlessRecyclerOnScrollListener(mLayoutManager!!) {
            override fun onLoadMore(current_page: Int) {
                mArticleViewModel?.listScrolled(current_page)
            }
        })
    }

    private fun removeScrollListener() {
        mRecyclerView?.clearOnScrollListeners()
    }

    private fun timeFilterFromClicked() {
        val dialog = DateDialog.newInstance(mArticleViewModel?.dateFrom!!, resources.getString(R.string.from_date))
        dialog.setListener(this)
        dialog.show(fragmentManager, "dialog")
    }

    private fun timeFilterToClicked() {
        val dialog = DateDialog.newInstance(mArticleViewModel?.dateTo!!, resources.getString(R.string.to_date))
        dialog.setListener(this)
        dialog.show(fragmentManager, "dialog")
    }

    override fun setDate(d: Date, s: String?) {
        if (s.equals(resources.getString(R.string.from_date))) {
            mArticleViewModel?.fromDateSet(d)
        } else if (s.equals(resources.getString(R.string.to_date))) {
            mArticleViewModel?.toDateSet(d)
        }
    }

    override fun onItemClicked(article: Article?) {
        val intent = Intent(activity, ArticleActivity::class.java)
        intent.putExtra(ArticleActivity.ARGS_ARTICLE, article)
        startActivity(intent)
        CustomIntent.customType(activity, "fadein-to-fadeout")
    }

    override fun onFavoriteButtonClicked(article: Article?) {
        mArticleViewModel?.favoriteButtonClicked(article)
    }

    override fun onShareClicked(article: Article?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check this out: " + article?.url)
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }

    override fun onListUpdated(articles: List<Article>) {
        mAdapter?.setArticles(articles)
    }
}