package com.strish.android.testproject.adapters

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.strish.android.testproject.adapters.ArticlesAdapter.ArticlesViewHolder

import java.io.ByteArrayOutputStream

class ArticlesAdapter(private var mArticles: List<Article>?) : RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder>() {
    private var mListener: OnItemClickedListener? = null

    interface OnItemClickedListener {
        fun onItemClicked(article: Article?)

        fun onFavoriteButtonClicked(article: Article?)

        fun onShareClicked(article: Article?)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ArticlesViewHolder(v, mListener)
    }

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        val mArticle = mArticles!![position]
        holder.bindArticle(mArticle)
    }

    override fun getItemCount(): Int {
        return mArticles!!.size
    }

    fun setArticles(articles: List<Article>) {
        mArticles = articles
        notifyDataSetChanged()
    }


    class ArticlesViewHolder(itemView: View, listener: OnItemClickedListener?) : RecyclerView.ViewHolder(itemView) {
        /*
        * findViewById can be replaced with kotlin synthetic
        */
        private var mImageView: ImageView = itemView.findViewById(R.id.image_view_list)
        private var mTitleTextView: TextView = itemView.findViewById(R.id.list_item_title_text_view)
        private var mBylineTextView: TextView = itemView.findViewById(R.id.list_item_byline_text_view)
        private var mFavoriteButton: ImageButton = itemView.findViewById(R.id.list_item_favorites_button)
        private var mShareButton: ImageButton = itemView.findViewById(R.id.list_item_share_button)

        private var mArticle: Article? = null

        init {
            itemView.setOnClickListener {
                listener?.onItemClicked(mArticle)
            }

            mFavoriteButton.setOnClickListener {
                if (listener != null) {
                    val bitmap = (mImageView.drawable as BitmapDrawable).bitmap
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val imageInByte = baos.toByteArray()
                    mArticle?.thumbnailByte = imageInByte
                    listener.onFavoriteButtonClicked(mArticle)
                    mFavoriteButton.setImageResource(R.drawable.ic_favorite_on)
                }
            }

            mShareButton.setOnClickListener {
                listener?.onShareClicked(mArticle)
            }
        }

        fun bindArticle(article: Article) {
            mArticle = article
            Picasso.get().load(article.urlToImage).fit().centerCrop().into(mImageView)
            mTitleTextView.text = article.title
            mBylineTextView.text = article.author

            if (article.favorite) {
                mFavoriteButton.setImageResource(R.drawable.ic_favorite_on)
            } else {
                mFavoriteButton.setImageResource(R.drawable.ic_favorite_off)
            }
        }

    }

}
