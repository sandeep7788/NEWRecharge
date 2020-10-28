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
import com.example.myrecharge.Models.PayReportDeatilsModel
import com.example.myrecharge.R
import java.util.*

public class Pay_Adapter(
    context: Context,
    movies: MutableList<PayReportDeatilsModel>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var movies: MutableList<PayReportDeatilsModel>?
    private val context: Context
    private var isLoadingAdded = false
    var TAG = "@@PagingAdapter"
    fun getMovies(): List<PayReportDeatilsModel>? {
        return movies
    }

    fun setMovies(movies: MutableList<PayReportDeatilsModel>?) {
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
        val v1 = inflater.inflate(R.layout.pay_adapter, parent, false)
        viewHolder = ItemList(v1)
        return viewHolder
    }

   

    override fun getItemCount(): Int {
        return if (movies == null) 0 else movies!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movies!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(mc: PayReportDeatilsModel) {
        movies!!.add(mc)
        notifyItemInserted(movies!!.size - 1)
    }

    fun addAll(mcList: List<PayReportDeatilsModel>) {
        for (mc in mcList) {
            add(mc)
        }
    }

    fun remove(city: PayReportDeatilsModel?) {
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
        add(PayReportDeatilsModel())
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

    fun getItem(position: Int): PayReportDeatilsModel {
        return movies!![position]
    }

    protected inner class ItemList(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val txt_id: TextView
        val img_operator: ImageView
        val txt_recharge: TextView
        val txt_status: TextView
        val txt_date: TextView

        init {
            txt_id = itemView.findViewById<View>(R.id.txt_id) as TextView
            img_operator =
                itemView.findViewById<View>(R.id.img_operator) as ImageView
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
                val mItemList =
                    holder as ItemList?
                try {
                    if (movie.eWalletTransactionID == null) {
//                    mItemList.txt_id.setText("_");
                    } else {

                        mItemList!!.txt_id.text = "E-TransactionID : "+movie.eWalletTransactionID.toString()
                        mItemList.txt_date.text = "on " + movie.adddate.toString()
                        mItemList.txt_recharge.text =
                            "Recharge of " + movie.amount.toString()
                        mItemList.txt_status.text = "Transtime : " + movie.transtime

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