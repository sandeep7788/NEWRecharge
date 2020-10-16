package com.example.myrecharge.Activitys.Manus

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.budiyev.android.codescanner.*
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Permission_Advance
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityScannerBinding

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

//        start_scaner()
        checkPermission(arrayOf(
            Manifest.permission.CAMERA),101)
    }

    override fun onResume() {
        super.onResume()
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
    fun checkPermission(permission: Array<out String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@Scanner_Activity, permission[0]) === PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@Scanner_Activity, permission,
                requestCode
            )
        } else {
            Toast.makeText(
                this@Scanner_Activity,
                "Permission already granted",
                Toast.LENGTH_SHORT
            ).show()
            start_scaner()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                    "Camera Permission Granted",
                    Toast.LENGTH_SHORT)
                    .show();
            }
            else {
                Toast.makeText(this,
                    "Camera Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
            }
        }
    }
}