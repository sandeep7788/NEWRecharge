package com.example.myrecharge.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.R
import com.example.myrecharge.databinding.*


class Transfer_Fragment: Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentTransferBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_transfer,container, false)
        thiscontext=container!!.context





        return mainBinding.root
    }

}