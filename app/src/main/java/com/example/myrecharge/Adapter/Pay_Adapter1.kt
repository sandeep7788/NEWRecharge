package com.example.myrecharge.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myrecharge.Models.RechargePlaneModel
import com.example.myrecharge.R
import java.util.*

public class Pay_Adapter1(
    context: Context,
    movies: MutableList<RechargePlaneModel>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var movies: MutableList<RechargePlaneModel>?
    private val context: Context
    private var isLoadingAdded = false
    var TAG = "@@PagingAdapter"
    fun getMovies(): List<RechargePlaneModel>? {
        return movies
    }

    fun setMovies(movies: MutableList<RechargePlaneModel>?) {
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
            ITEM -> viewHolder =
                getViewHolder(parent, inflater)
            LOADING -> {
                val v2 = inflater.inflate(R.layout.item_progress, parent, false)
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
        val v1 = inflater.inflate(R.layout.item_list1, parent, false)
        viewHolder = ItemList(v1)
        return viewHolder
    }

   

    override fun getItemCount(): Int {
        return if (movies == null) 0 else movies!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movies!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(mc: RechargePlaneModel) {
        movies!!.add(mc)
        notifyItemInserted(movies!!.size - 1)
    }

    fun addAll(mcList: List<RechargePlaneModel>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    fun remove(city: RechargePlaneModel?) {
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
        add(RechargePlaneModel())
    }
    fun removeLoadingFooter1() {
        isLoadingAdded = false
       /* val position = movies!!.size - 1
        val item = getItem(position)
        if (item != null) {
            movies!!.removeAt(position)
            notifyItemRemoved(position)
        }*/
        notifyDataSetChanged()
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

    fun getItem(position: Int): RechargePlaneModel {
        return movies!![position]
    }

    protected class ItemList(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public val txt_id: TextView
        public val img_operator: ImageView
        val txt_recharge: TextView
        public val txt_status: TextView
        public val txt_date: TextView

        init {
            txt_id = itemView.findViewById<View>(R.id.txt_id) as TextView
            img_operator = itemView.findViewById<View>(R.id.img_operator) as ImageView
            txt_recharge = itemView.findViewById<View>(R.id.txt_recharge) as TextView
            txt_status = itemView.findViewById<View>(R.id.txt_status) as TextView
            txt_date = itemView.findViewById<View>(R.id.txt_date) as TextView
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
                try {
                    val mItemList =
                        holder as ItemList?

                    if (movie.transID == null && movie.transID.isEmpty()) {
//                    mItemList.txt_id.setText("_");
                    } else {
                        try {
                            mItemList!!.txt_id.text = movie.mobileno.toString() + " "
                            mItemList.txt_date.setText("on " + movie.addDate.toString() + " " + movie.rechargetime.toString())
                            mItemList.txt_recharge.setText("₹ " + movie.rechargeAmount.toString())
                            mItemList.txt_status.setText(" " + movie.status)
                            Glide.with(context)
                                .load(movie.operaotimageurl)
                                .thumbnail(0.5f)
                                .into(mItemList.img_operator)
                            if (movie.status.toString().toLowerCase() == "success") {
                                mItemList.txt_status.setTextColor(Color.parseColor("#0b892e"))
                            } else {
                                mItemList.txt_status.setTextColor(Color.parseColor("#ed1b24"))
                            }
                        } catch (e: Exception) {
//                    Toast.makeText(context, " @@Exception" + e.message, Toast.LENGTH_SHORT).show()
                            Log.d(">>-----", "end1" + e.message.toString())
                        }
                    }
                } catch (e: Exception) {
//                    Toast.makeText(context, " @@Exception" + e.message, Toast.LENGTH_SHORT).show()
                    Log.d(">>-----", "end1" + e.message.toString())
                }
            }
            LOADING -> {
            }
        }
    }
}