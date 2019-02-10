package com.strish.android.testproject.adapters

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso
import com.strish.android.testproject.Article
import com.strish.android.testproject.R
import com.strish.android.testproject.adapters.FavoritesAdapter.ArticlesViewHolder


class FavoritesAdapter : ListAdapter<Article, FavoritesAdapter.ArticlesViewHolder>(DIFF_CALLBACK) {
    private var mListener: OnItemClickedListener? = null

    interface OnItemClickedListener {
        fun onItemClicked(article: Article?)

        fun onFavoriteButtonClicked(article: Article?)

        fun onShareClicked(article: Article?)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        mListener = listener
    }

    class ArticlesViewHolder(itemView: View, listener: OnItemClickedListener?) : RecyclerView.ViewHolder(itemView) {
        var mImageView: ImageView
        var mTitleTextView: TextView
        var mBylineTextView: TextView
        var mFavoriteButton: ImageButton
        var mShareButton: ImageButton
        private var mArticle: Article? = null

        init {
            mImageView = itemView.findViewById(R.id.image_view_list)
            mTitleTextView = itemView.findViewById(R.id.list_item_title_text_view)
            mBylineTextView = itemView.findViewById(R.id.list_item_byline_text_view)
            mFavoriteButton = itemView.findViewById(R.id.list_item_favorites_button)
            mShareButton = itemView.findViewById(R.id.list_item_share_button)

            mFavoriteButton.setImageResource(R.drawable.ic_favorite_on)

            itemView.setOnClickListener {
                listener?.onItemClicked(mArticle)
            }

            mFavoriteButton.setOnClickListener {
                listener?.onFavoriteButtonClicked(mArticle)
            }

            mShareButton.setOnClickListener {
                listener?.onShareClicked(mArticle)
            }
        }

        fun bindArticle(article: Article) {
            mArticle = article
            Picasso.get().load(mArticle!!.urlToImage).fit().centerCrop().into(mImageView)
            mTitleTextView.text = mArticle!!.title
            mBylineTextView.text = mArticle!!.author
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ArticlesViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val mArticle = getItem(position)
        holder.bindArticle(mArticle)
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return true
            }
        }
    }

}
