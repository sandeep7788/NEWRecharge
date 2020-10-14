package com.example.myrecharge.Activitys.Manus

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPrepaidBinding

class Prepaid_Activity : AppCompatActivity() {
    lateinit var mainBinding: ActivityPrepaidBinding
    var pref = Local_data(this@Prepaid_Activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding =
            DataBindingUtil.setContentView(this,R.layout.activity_prepaid)
        pref.setMyappContext(this@Prepaid_Activity)

        monClick()

    }
    fun monClick()
    {
        mainBinding.toolbar.back.setOnClickListener { super.onBackPressed() }
    }
}