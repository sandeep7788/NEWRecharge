package com.example.myrecharge.Helper

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.util.Log
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Helper.RetrofitManager1.getInstance
import de.mateware.snacky.Snacky

public open class MyReceiver() : BroadcastReceiver() {
    var pausingDialog:SweetAlertDialog?=null
    override fun onReceive(context: Context, intent: Intent) {

        ObservableObject.getInstance().updateValue(intent)
        var status: String = NetworkUtil.getConnectivityStatusString(context)!!

        if(status.equals("No internet is available"))
        {

            blockActivity(false,context)
        }else{
            Log.d("@@", "onReceive: 3")
            /*blockActivity(true,context)
            Snacky.builder()
                .setActivity(context as Activity?)
                .setActionText("Hide")
                .setActionTextColor(Color.parseColor("#ffffff"))
                .setActionTextColor(Color.parseColor("#5D6D7E"))
                .setText(status)
                .setTextColor(Color.parseColor("#ffffff"))
                .setBackgroundColor(Color.parseColor("#041e37"))
                .setDuration(Snacky.LENGTH_INDEFINITE)
                .build()

                .show()*/
        }
    }


    fun blockActivity(connected: Boolean,context: Context) {

        if (pausingDialog == null) {
            pausingDialog =SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            pausingDialog!!.titleText = "Application waiting for internet connection..."
            pausingDialog!!.setCancelable(false)
            pausingDialog!!.setConfirmClickListener{
                var MyReceiver: BroadcastReceiver?= null;
                MyReceiver = com.example.myrecharge.Helper.MyReceiver();
                context.registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
        if (!connected) {
            if (!(context as Activity).isFinishing) {
                pausingDialog!!.show()
            }
        } else {
            pausingDialog!!.dismiss()
        }
    }
}