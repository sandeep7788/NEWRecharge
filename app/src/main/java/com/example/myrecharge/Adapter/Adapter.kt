package com.example.myrecharge.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentAdapterBinding


class Adapter : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentAdapterBinding
    var data:ArrayList<String> ?=null

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment

        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_adapter, container, false)
        thiscontext=container!!.context
        data= ArrayList()


        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")
        data!!.add("\n 9898989080")




//        layoutManager = LinearLayoutManager(this@Adapter)
//        mainBinding.Recyclerview.layoutManager = layoutManager

        mainBinding.Recyclerview.layoutManager = LinearLayoutManager(thiscontext, LinearLayout.VERTICAL, false)
        mainBinding.Recyclerview.adapter =
            ListAdapter1(data!!, thiscontext)

        return mainBinding.root
    }
}