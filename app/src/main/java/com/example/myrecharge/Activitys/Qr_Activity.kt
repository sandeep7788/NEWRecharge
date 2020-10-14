package com.example.myrecharge.Activitys

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class Qr_Activity : AppCompatActivity() {
    lateinit var mainBinding : FragmentQrcodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.fragment_qrcode
            )

        genrate_qr_code()

        mainBinding.btnBack.setOnClickListener {
            finish()
        }


    }
    fun genrate_qr_code()
    {
        var pref= Local_data(this@Qr_Activity)
        pref.setMyappContext(this@Qr_Activity)
        val content = pref.ReadStringPreferences(Constances.PREF_Login_Id)

        mainBinding.tAccountno.setText(pref.ReadStringPreferences(Constances.PREF_Login_Id) + " ")
        mainBinding.tName.setText(pref.ReadStringPreferences(Constances.PREF_MemberName) + " ")


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
        mainBinding.iQrcode.setImageBitmap(bitmap)
    }
}