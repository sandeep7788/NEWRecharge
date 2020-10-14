package com.example.myrecharge.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentProfileBinding

class Profile_Fragment : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_profile, container, false)
        thiscontext=container!!.context
        mainBinding.toolbarLayout.back.visibility=View.GONE

        return mainBinding.root
    }

/*
    fun listeners()
    {
        mainBinding.btnPersnalDetails.setOnClickListener {
            startActivity(Intent(thiscontext,Personal_detailsActivity::class.java))
        }

        mainBinding.btnAccountDetails.setOnClickListener {
            startActivity(Intent(thiscontext,AccountDetails_Activity::class.java))
        }

        mainBinding.btnChangePassword.setOnClickListener {
            startActivity(Intent(thiscontext,ChangePassword_Activity::class.java))
        }

    }
*/
}