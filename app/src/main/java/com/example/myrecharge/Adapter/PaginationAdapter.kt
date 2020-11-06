package com.example.myrecharge.Adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.myrecharge.Helper.PaginationAdapterCallback
import com.example.myrecharge.Models.OperatorModel
import com.example.myrecharge.R

class PaginationAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private val ITEM: Int = 0
    private val LOADING = 1
    private val HERO = 2
    var TAG="!!"
    private val BASE_URL_IMG = "https://image.tmdb.org/t/p/w200"

    var movieOperatorModels: MutableList<OperatorModel>? =
        null
    private var context: Context? = null

    private var isLoadingAdded = false
    private var retryPageLoad = false

    private var mCallback: PaginationAdapterCallback? = null

    private var errorMsg: String? = null

constructor(context: Context?,movieOperatorModels: MutableList<OperatorModel>?) {
        this.context = context
        this.movieOperatorModels=movieOperatorModels
        mCallback = context as PaginationAdapterCallback?

    }

    fun getMovies(): List<OperatorModel>? {
        return movieOperatorModels
    }

    fun setMovies(movieOperatorModels: MutableList<OperatorModel>?) {
        this.movieOperatorModels = movieOperatorModels
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        Log.d(TAG, "onCreateViewHolder: "+viewType.toString())
        movieOperatorModels = ArrayList()
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem =
                    inflater.inflate(R.layout.item_list, parent, false)
                viewHolder = MovieVH(viewItem)
            }
            LOADING -> {
                val viewLoading =
                    inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
            HERO -> {
                val viewLoading =
                    inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(viewLoading)
            }
        }
        return viewHolder!!
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = movieOperatorModels!![position] // Movie
        when (getItemViewType(position)) {
            HERO -> {

                val heroVh: HeroVH = holder as HeroVH

                heroVh.mMovieTitle.text = result.operatorName
                heroVh.mYear.text = formatYearLabel(result)
                heroVh.mMovieDesc.text = result.operatorName
                loadImage(result.operaotimageurl)
                    .into(heroVh.mPosterImg)
            }

            LOADING -> {
                val loadingVH = holder as LoadingVH
                if (retryPageLoad) {
                    loadingVH.mErrorLayout.visibility = View.VISIBLE
                    loadingVH.mProgressBar.visibility = View.GONE
                    loadingVH.mErrorTxt.text = "Error...."
                } else {
                    loadingVH.mErrorLayout.visibility = View.GONE
                    loadingVH.mProgressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (movieOperatorModels == null) 0 else movieOperatorModels!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HERO
        } else {
            if (position == movieOperatorModels!!.size - 1 && isLoadingAdded) LOADING else ITEM
        }
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */
    /**
     * @param result
     * @return [releasedate] | [2letterlangcode]
     */
    private fun formatYearLabel(result: OperatorModel): String? {
        return (result.operatorCode.substring(0, 4) // we want the year only
                + " | "
                + result.operatorName.toUpperCase())
    }

    /**
     * Using Glide to handle image loading.
     * Learn more about Glide here:
     * [](http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/)
     *
     *
     * //     * @param posterPath from [OperatorModel.getPosterPath]
     *
     * @return Glide builder
     */
    private fun loadImage(posterPath: String): RequestBuilder<Drawable?> {
        return Glide
            .with(context!!)
            .load(BASE_URL_IMG + posterPath)
            .centerCrop()
    }


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */
    fun add(r: OperatorModel) {
        movieOperatorModels!!.add(r)
        notifyItemInserted(movieOperatorModels!!.size - 1)
    }

    fun addAll(moveOperatorModels: List<OperatorModel>) {
        for (result in moveOperatorModels) {
            add(result)
        }
    }

    fun remove(r: OperatorModel?) {
        val position = movieOperatorModels!!.indexOf(r)
        if (position > -1) {
            movieOperatorModels!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(OperatorModel())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieOperatorModels!!.size - 1
        val result = getItem(position)
        if (result != null) {
            movieOperatorModels!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): OperatorModel? {
        return movieOperatorModels!![position]
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(movieOperatorModels!!.size - 1)
        if (errorMsg != null) this.errorMsg = errorMsg
    }


    /*
   View Holders
   _________________________________________________________________________________________________
    */

    /*
   View Holders
   _________________________________________________________________________________________________
    */
    /**
     * Header ViewHolder
     */
    public class HeroVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val mMovieTitle: TextView
        val mMovieDesc: TextView
        val mYear // displays "year | language"
                : TextView
        val mPosterImg: ImageView

        init {
            mMovieTitle = itemView.findViewById(R.id.movie_title)
            mMovieDesc = itemView.findViewById(R.id.movie_desc)
            mYear = itemView.findViewById(R.id.movie_year)
            mPosterImg = itemView.findViewById(R.id.movie_poster)
        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val mMovieTitle: TextView
        val mMovieDesc: TextView
        val mYear // displays "year | language"
                : TextView
        val mPosterImg: ImageView
        val mProgress: ProgressBar

        init {
            mMovieTitle = itemView.findViewById(R.id.movie_title)
            mMovieDesc = itemView.findViewById(R.id.movie_desc)
            mYear = itemView.findViewById(R.id.movie_year)
            mPosterImg = itemView.findViewById(R.id.movie_poster)
            mProgress = itemView.findViewById(R.id.movie_progress)
        }
    }


    protected class LoadingVH(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val mProgressBar: ProgressBar
        private val mRetryBtn: ImageButton
        val mErrorTxt: TextView
        val mErrorLayout: LinearLayout
        override fun onClick(view: View) {
            when (view.id) {
                R.id.loadmore_retry, R.id.loadmore_errorlayout -> {
//                    showRetry(false, null)
//                    mCallback.retryPageLoad()
                }
            }
        }

        init {
            mProgressBar = itemView.findViewById(R.id.loadmore_progress)
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry)
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt)
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout)
            mRetryBtn.setOnClickListener(this)
            mErrorLayout.setOnClickListener(this)
        }
    }
}