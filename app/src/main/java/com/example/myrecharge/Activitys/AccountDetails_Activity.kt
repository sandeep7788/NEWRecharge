package com.example.myrecharge.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityAccountDetailsBinding

class AccountDetails_Activity : AppCompatActivity() {
    lateinit var mainBinding : ActivityAccountDetailsBinding
    var pref= Local_data(this@AccountDetails_Activity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_account_details_)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_account_details_
            )
        pref.setMyappContext(this@AccountDetails_Activity)

        set_data()
        set_editable_false()
        set_listeners()
    }

    fun set_data()
    {
//        mainBinding.toolbar.tTitle.text = "Account Details"
        mainBinding.edtName.setText(pref.ReadStringPreferences(Constances.PREF_MemberName))
        mainBinding.edtAddress.setText(pref.ReadStringPreferences(Constances.PREF_Address))
        mainBinding.edtMobileno.setText(pref.ReadStringPreferences(Constances.PREF_Mobile))

    }

    fun set_editable_false()
    {
        mainBinding.edtName.isFocusable = false;
        mainBinding.edtName.isFocusableInTouchMode = false
        mainBinding.edtName.isClickable = false

        mainBinding.edtMobileno.isFocusable = false;
        mainBinding.edtMobileno.isFocusableInTouchMode = false
        mainBinding.edtMobileno.isClickable = false

        mainBinding.edtName.isFocusable = false;
        mainBinding.edtName.isFocusableInTouchMode = false
        mainBinding.edtName.isClickable = false

        mainBinding.edtName.isFocusable = false;
        mainBinding.edtName.isFocusableInTouchMode = false
        mainBinding.edtName.isClickable = false
    }

    fun set_listeners()
    {
        mainBinding.toolbar.back.setOnClickListener {
            finish()
        }
        mainBinding.btnSubmit.setOnClickListener {
            Toast.makeText(this@AccountDetails_Activity,"Please Try Later....", Toast.LENGTH_LONG).show()
        }
    }
}