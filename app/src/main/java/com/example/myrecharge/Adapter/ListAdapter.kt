package com.example.myrecharge.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecharge.R
import com.squareup.picasso.Picasso

class ListAdapter(private val list: List<String>, var context:Context)
    : RecyclerView.Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {


/*
        Picasso.with(context)
            .load(list.get(position).icon1)
            .into(holder.i_image)
*/
    }

    override fun getItemCount(): Int = list.size

}
class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.list_adapter, parent, false)) {
    var i_image: ImageView? = null
    var t_date: TextView? = null
    var amount1: TextView? = null
    var t_description: TextView? = null

    init {
        i_image = itemView.findViewById(R.id.i_image)
        t_date = itemView.findViewById(R.id.t_date)
        amount1 = itemView.findViewById(R.id.amount1)
        t_description= itemView.findViewById(R.id.t_description)

    }


}