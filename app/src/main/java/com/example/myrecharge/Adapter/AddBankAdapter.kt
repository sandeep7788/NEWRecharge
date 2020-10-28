package com.example.myrecharge.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecharge.Models.BankListModel
import com.example.myrecharge.R


public class AddBankAdapter(private val list: List<BankListModel>, var context:Context)
    : RecyclerView.Adapter<BankHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BankHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: BankHolder, position: Int) {
        holder.t_description!!.setText(list.get(position).bankerMasterName.toString())


        holder!!.t_description!!.setOnClickListener {
            val popup = PopupMenu(context, holder.t_description)
            popup.inflate(R.menu.navigation)
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(p0: MenuItem?): Boolean {
                    Log.e(">>",p0.toString())
                    return true
                }

            })
            popup.show();
            }
    }

    override fun getItemCount(): Int = list.size

}

class BankHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.bank_list_adapter, parent, false)) {
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