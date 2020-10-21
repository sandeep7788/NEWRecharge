package com.example.myrecharge.Fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.Adapter.Adapter
import com.example.myrecharge.R
import com.example.myrecharge.databinding.*


class LandlineFragment: Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentLandlineBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_landline, container, false)
        thiscontext=container!!.context


        mainBinding.ivPhoneBook.setOnClickListener {
            browseContacts()
        }

//        setFram(Adapter())



        return mainBinding.root
    }
    private fun browseContacts() {
        val pickContactIntent =
            Intent("android.intent.action.PICK", Uri.parse("content://contacts"))
        pickContactIntent.type = "vnd.android.cursor.dir/phone_v2"
        startActivityForResult(pickContactIntent, 1)
    }

    fun setFram(fram: Fragment)
    {


        val newFragment = fram
        requireFragmentManager().beginTransaction()
            .replace(R.id.f1,newFragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }

}