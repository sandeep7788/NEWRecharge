package com.example.myrecharge.Custom_

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QrDialog(var activity: Activity?):Dialog(activity!!) {

    private var title = ""
    private var text = ""
    private var dialogId = -1
    private var buttonName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_dialog)

        val layoutParams = window!!.attributes
        window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window!!.attributes = layoutParams
        setCancelable(false)
        var img_qrcode: ImageView? =findViewById<ImageView>(R.id.img_qrcode)
        var img_close: ImageView? =findViewById<ImageView>(R.id.img_close)
        var txt_name: TextView? =findViewById<TextView>(R.id.txt_name)
        img_close?.setOnClickListener { dismiss() }
        if (img_qrcode != null) {
            showQrCode(img_qrcode,txt_name!!)
            Log.e(">>","if")
        }
        else {
            Log.e(">>","else")
        }
    }

    override fun onStart() {

        super.onStart()
    }

    fun showQrCode(img:ImageView,name:TextView) {

        var pref= Local_data(activity)
        pref.setMyappContext(activity)
        val content = pref.ReadStringPreferences(Constances.PREF_MemberID)
        name.text= pref.ReadStringPreferences(Constances.PREF_MemberName)

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        img!!.setImageBitmap(bitmap)
    }


}