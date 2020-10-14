package com.example.myrecharge.Activitys

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPay2Binding

class Pay_Activity2 : AppCompatActivity() {
    lateinit var mainBinding: ActivityPay2Binding
    var mid:String="0"
    var mname:String="0"
    var pref= Local_data(this@Pay_Activity2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_pay2)
        pref.setMyappContext(this@Pay_Activity2)
        setdata()
        mainBinding.btnBack.setOnClickListener { finish() }
        mainBinding.btnPay.setOnClickListener {

            if(mainBinding.edtAmount.text.toString().isEmpty())
            {
                mainBinding.edtAmount.setError("Enter value")
            }
            else if(mainBinding.edtPassword.text.toString().isEmpty())
            {
                mainBinding.edtPassword.visibility= View.GONE
            }
            else
            {
//                api_for_pay()
            }

        }

    }

    fun setdata()
    {
        mid=intent.getStringExtra("id").toString()
        mname=intent.getStringExtra("name").toString()

        mainBinding.tName.setText(mname)
        mainBinding.tdescription.setText("Rippo Wallet Linked to "+mid)
//        mainBinding.tBalance.setText(pref.ReadStringPreferences(Constances.PREF_balance2))

    }


    fun cln()
    {
        mainBinding.edtPassword.setText("")
        mainBinding.edtAmount.setText("")
    }
}