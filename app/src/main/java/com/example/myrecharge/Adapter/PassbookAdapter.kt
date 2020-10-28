package com.example.myrecharge.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrecharge.Models.HistoryModel
import com.example.myrecharge.R
import java.util.*

public class PassbookAdapter(context: Context, movies: MutableList<HistoryModel>?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var movies: MutableList<HistoryModel>?
    private val context: Context
    private var isLoadingAdded = false
    var TAG = "@@PagingAdapter"
    fun getMovies(): List<HistoryModel>? {
        return movies
    }

    fun setMovies(movies: MutableList<HistoryModel>?) {
        this.movies = movies
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        Log.d(TAG, "onCreateViewHolder: $viewType")
        when (viewType) {
            ITEM -> viewHolder = getViewHolder(parent, inflater)
            LOADING -> {
                val v2: View = inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }
        return viewHolder!!
    }

    private fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1: View = inflater.inflate(R.layout.passbook_adapter, parent, false)
        viewHolder = ItemList(v1)
        return viewHolder
    }



    override fun getItemCount(): Int {
        return if (movies == null) 0 else movies!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movies!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(mc: HistoryModel) {
        movies!!.add(mc)
        notifyItemInserted(movies!!.size - 1)
    }

    fun addAll(mcList: List<HistoryModel>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    fun remove(city: HistoryModel?) {
        val position = movies!!.indexOf(city)
        if (position > -1) {
            movies!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(HistoryModel())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movies!!.size - 1
        val item = getItem(position)
        if (item != null) {
            movies!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): HistoryModel {
        return movies!![position]
    }

    protected inner class ItemList(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txt_cr_dr: TextView
        val txt_title: TextView
        val txt_date: TextView
        val txt_Amount: TextView

        init {
            txt_cr_dr = itemView.findViewById<View>(R.id.txt_cr_dr) as TextView
            txt_title = itemView.findViewById<View>(R.id.txt_title) as TextView
            txt_date = itemView.findViewById<View>(R.id.txt_date) as TextView
            txt_Amount = itemView.findViewById<View>(R.id.txt_Amount) as TextView
        }
    }

    protected inner class LoadingVH(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!)

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }

    init {
        var movies = movies
        this.context = context
        this.movies = movies
        movies = ArrayList()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = movies!![position]
        when (getItemViewType(position)) {
            ITEM -> {
                val mItemList = holder as ItemList?
                try {
                    if (movie.eWalletTransactionID == null) {
//                    mItemList.txt_id.setText("_");
                    } else {
                        mItemList?.txt_date?.text = "on " + movie.adddate.toString()
                        mItemList?.txt_Amount?.text =
                            "₹ " + movie.amount.toString()
                        mItemList?.txt_cr_dr?.text = "" + movie.factor
                        mItemList?.txt_title?.text = movie.narration

                    }
                } catch (e: Exception) {
                    Toast.makeText(context, " " + e.message, Toast.LENGTH_SHORT).show()
                }
            }
            LOADING -> {
            }
        }
    }
}