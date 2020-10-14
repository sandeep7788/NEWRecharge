package com.example.myrecharge.Activitys

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityChangePasswordBinding

class ChangePassword_Activity : AppCompatActivity() {
    lateinit var mainBinding : ActivityChangePasswordBinding
    var pref= Local_data(this@ChangePassword_Activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_change_password_
            )
        pref.setMyappContext(this@ChangePassword_Activity)
        set_listeners()
    }

    fun set_listeners()
    {
        mainBinding.toolbar.back.setOnClickListener {
            finish()
        }
        mainBinding.btnSubmit.setOnClickListener {
            Toast.makeText(this@ChangePassword_Activity,"Please Try Later....", Toast.LENGTH_LONG).show()
        }
    }
}