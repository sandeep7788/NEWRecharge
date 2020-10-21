package com.example.myrecharge.Helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.myrecharge.Activitys.Manus.AllMenu_Activity

class Permission_Advance :AllMenu_Activity {
    private val PERMISSION_REQUEST_CODE = 101
    lateinit var context:Context
    var permission:String="0"

    constructor(context: Context)
    {
        this.context=context
    }

    public fun checkPermission(permission:String): Boolean {

        this.permission =permission;
        val result1 = ContextCompat.checkSelfPermission(context,
            permission
        )
        if((result1 == PackageManager.PERMISSION_GRANTED)==false)
        {

//            if(!(result1 == PackageManager.PERMISSION_GRANTED))
            requestPermission()
        }
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    public fun requestPermission() {

        requestPermissions(context as Activity,arrayOf(permission),PERMISSION_REQUEST_CODE)
//            onRequestPermissionsResult(101, 1)
    }

    public fun showMessageOKCancel(
        message: String) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   requestPermission()
                }
            })
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                // If request is cancelled, the result arrays are empty.

                if ((grantResults[0] == 1) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: if")
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: else")
                    showMessageOKCancel("You need to allow access to both the permissions")
                }
                return
            }
        }
    }

}