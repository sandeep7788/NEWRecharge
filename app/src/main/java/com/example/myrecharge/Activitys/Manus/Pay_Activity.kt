package com.example.myrecharge.Activitys.Manus

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.*
import com.example.myrecharge.Helper.*
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPayBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

open class Pay_Activity : AppCompatActivity()
{
    lateinit var mainBinding : ActivityPayBinding
    var pref= Local_data(this@Pay_Activity)
//    private lateinit var codeScanner: CodeScanner
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_pay
            )
        pref.setMyappContext(this@Pay_Activity)
//        start_scaner()

    mainBinding.toolbar.back.setOnClickListener { super.onBackPressed() }
    mainBinding.btnScan.setOnClickListener { startActivity(Intent(this@Pay_Activity,Scanner_Activity::class.java)) }

        mainBinding.btnPay.setOnClickListener {
            if(mainBinding.edtMemberId.text.toString().isEmpty())
            {
                mainBinding.edtMemberId.setError("Enter value")
            }
            else
            {

            }
        }
    }






    override fun onResume() {
        super.onResume()
//        codeScanner.startPreview()
    }

    override fun onPause() {
//        codeScanner.releaseResources()
        super.onPause()
    }
}