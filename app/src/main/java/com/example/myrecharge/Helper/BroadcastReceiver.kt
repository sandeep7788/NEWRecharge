package com.example.myrecharge.Helper

import android.app.Activity
import android.content.*
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Activitys.NetworkUtil


public open class MyReceiver() : BroadcastReceiver() {
    var pausingDialog:SweetAlertDialog?=null
    override fun onReceive(context: Context, intent: Intent) {

        var status: String = NetworkUtil.getConnectivityStatusString(
            context
        )!!
        if(status.equals("No internet is available"))
        {
            blockActivity(false,context)
        }else{
            Log.d("@@", "onReceive: 3")
            blockActivity(true,context)
            blockActivity(true,context)
/*
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
                .show()
*/
        }
    }

    fun blockActivity(connected: Boolean,context: Context) {

        if (pausingDialog == null) {
            pausingDialog =SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            pausingDialog!!.titleText = "Application waiting for internet connection..."
            pausingDialog!!.setCancelable(false)
            pausingDialog!!.setConfirmClickListener{
                pausingDialog!!.dismiss()
                var MyReceiver: BroadcastReceiver?= null;
                MyReceiver = MyReceiver();
                context.registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
                val intent = Intent()
                intent.component = ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings\$DataUsageSummaryActivity"
                )
                context.startActivity(intent)
            }
        }
        if (!connected) {
            if (!(context as Activity).isFinishing) {
                pausingDialog!!.show()
            }
        } else {
            if (pausingDialog!!.isShowing){
            pausingDialog!!.dismiss()}
        }
    }
}