package com.example.myrecharge.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.Activitys.DashboardActivity
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentQrcodeBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class Qr_codeFrgament: Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentQrcodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_qrcode, container, false
        )
        thiscontext=container!!.context
        mainBinding.btnBack.setOnClickListener {
            startActivity(Intent(thiscontext, DashboardActivity::class.java))
        }

        genrate_qr_code()

        return mainBinding.root
    }

    private fun browseContacts() {
        val pickContactIntent =
            Intent("android.intent.action.PICK", Uri.parse("content://contacts"))
        pickContactIntent.type = "vnd.android.cursor.dir/phone_v2"
        startActivityForResult(pickContactIntent, 1)
    }
    fun genrate_qr_code()
    {
        var pref= Local_data(thiscontext)
        pref.setMyappContext(thiscontext)
        val content = pref.ReadStringPreferences(Constances.PREF_Login_Id)

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