package com.example.myrecharge.Activitys.Manus

import android.Manifest.permission.*
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Permission_Advance
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityScannerBinding
import kotlinx.android.synthetic.main.activity_scanner_.*

class Scanner_Activity : AppCompatActivity() {
    lateinit var mainBinding : ActivityScannerBinding
    var pref= Local_data(this@Scanner_Activity)
    private lateinit var codeScanner: CodeScanner
    var p=Permission_Advance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding =
            DataBindingUtil.setContentView(this,R.layout.activity_scanner_)
        pref.setMyappContext(this@Scanner_Activity)
        codeScanner = CodeScanner(this,mainBinding.scannerView)


        if(p.checkPermission(CAMERA))
        {
            start_scaner()
                    Log.e("@@scanerActivity","if condition")
        }
    }

    override fun onResume() {
        super.onResume()
//        if(p.checkPermission(CAMERA))
                codeScanner.startPreview()
        }

    fun start_scaner()
    {
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()



            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        mainBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    override fun onPause() {
        var p=Permission_Advance(this)
        codeScanner.releaseResources()
        super.onPause()
    }
}