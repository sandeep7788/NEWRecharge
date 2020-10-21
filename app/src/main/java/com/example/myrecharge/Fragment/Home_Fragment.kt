package com.example.myrecharge.Fragment

import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
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
import com.example.myrecharge.Activitys.Manus.AllMenu_Activity
import com.example.myrecharge.Activitys.Manus.Prepaid_Activity
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
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
        initView()
        monClick()

        return mainBinding.root
    }
    fun initView(){
        mainBinding.txtBalance.text="₹ "+pref.ReadStringPreferences(Constances.PREF_Balance)
    }
    fun monClick()
    {
        var intent:Intent ?=null
        intent=Intent(context,Prepaid_Activity::class.java)
        mainBinding.btnPrepaid.setOnClickListener {
            intent!!.putExtra("opt",1)
            startActivity(intent) }

        mainBinding.btnPostpaid.setOnClickListener {
            intent!!.putExtra("opt",2)
            startActivity(intent) }

        mainBinding.btnDth.setOnClickListener {
            intent!!.putExtra("opt",3)
            startActivity(intent) }

        mainBinding.btnElectric.setOnClickListener {
            intent!!.putExtra("opt",4)
            startActivity(intent) }

        mainBinding.btnMTransfer.setOnClickListener {
            intent!!.putExtra("opt",5)
            startActivity(intent) }

        mainBinding.btnBus.setOnClickListener {
            intent!!.putExtra("opt",6)
            startActivity(intent) }

        mainBinding.btnFlight.setOnClickListener {
            intent!!.putExtra("opt",7)
            startActivity(intent) }

        mainBinding.btnDatacard.setOnClickListener {
            intent!!.putExtra("opt",8)
            startActivity(intent) }

        mainBinding.btnGas.setOnClickListener {
            intent!!.putExtra("opt",10)
            startActivity(intent) }

        mainBinding.btnPay.setOnClickListener { startActivity(Intent(context,AllMenu_Activity::class.java)) }
    }

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
    private fun createBottomUpAnimation(
        view: View,
        listener: AnimatorListenerAdapter?, distance: Float
    ): ObjectAnimator? {
        val animator = ObjectAnimator.ofFloat(view, "translationY", -distance)
        //        animator.setDuration(???)
        animator.removeAllListeners()
        if (listener != null) {
            animator.addListener(listener)
        }
        return animator
    }
    fun createTopDownAnimation(
        view: View, listener: AnimatorListenerAdapter?,
        distance: Float
    ): ObjectAnimator? {
        view.translationY = -distance
        val animator = ObjectAnimator.ofFloat(view, "translationY", 0f)
        animator.removeAllListeners()
        if (listener != null) {
            animator.addListener(listener)
        }
        return animator
    }
/*
    fun slid() {
        createTopDownAnimation(mainBinding.topLayout, null,100).start())
        createBottomUpAnimation(mainBinding.topLayout, null, mainBinding.topLayout.getHeight()).start();
    }
*/
}