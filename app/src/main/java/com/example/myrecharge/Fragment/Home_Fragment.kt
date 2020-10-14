package com.example.myrecharge.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myrecharge.Activitys.Manus.Pay_Activity
import com.example.myrecharge.Activitys.Manus.Prepaid_Activity
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentHomeBinding
import com.sandeep.AndroidDialog.AndroidDialog

class Home_Fragment: Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentHomeBinding
    lateinit var transaction: FragmentTransaction
    var pref= Local_data(context)
    override fun onCreateView(inflater:LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View?
    {
        pref.setMyappContext(context)
        mainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )
        thiscontext=container!!.context
        monClick()

        return mainBinding.root
    }
    fun monClick()
    {
        mainBinding.btnPrepaid.setOnClickListener { startActivity(Intent(context,Prepaid_Activity::class.java)) }
        mainBinding.btnPay.setOnClickListener { startActivity(Intent(context,Pay_Activity::class.java)) }
    }

/*
    fun setFram(fram: Fragment)
    {


        val newFragment = fram
        requireFragmentManager().beginTransaction()
            .replace(R.id.swipe, newFragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }
*/
    fun setFram1(fram: Fragment)
    {
        val newFragment = fram
        requireFragmentManager().beginTransaction()
            .replace(R.id.frame, newFragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }


    fun dialog()
    {
        val dialog = AndroidDialog(thiscontext)
        dialog.setColor(Color.parseColor("#041e37"))
        dialog.setTitle("Your Current Balance")
        dialog.setColor(R.color.colorPrimaryDark)
        dialog.setContentTextColor(R.color.colorPrimaryDark)
        dialog.contentText = pref.ReadStringPreferences(Constances.PREF_Balance)+" "
        dialog.setPositiveListener(
            "Ok"
        ) { dialog ->
            Toast.makeText(
                thiscontext,
                dialog.positiveText.toString(),
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }.show()

    }

    fun try_later(titile:String)
    {
        val dialog = AndroidDialog(thiscontext, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
        dialog.setColor(Color.parseColor("#041e37"))
        dialog.setTitle(titile)
        dialog.setColor(R.color.colorPrimaryDark)
        dialog.setContentTextColor(R.color.colorPrimaryDark)
        dialog.setPositiveListener(
            "Ok"
        ) { dialog ->
            Toast.makeText(
                thiscontext,
                dialog.positiveText.toString(),
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }.show()
    }
}