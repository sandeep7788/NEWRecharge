package com.example.myrecharge.Activitys

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.myrecharge.Fragment.*
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class DashboardActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityDashboardBinding
    lateinit var transaction:FragmentTransaction
    var pref= Local_data(this@DashboardActivity)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        pref.setMyappContext(this@DashboardActivity)
        mainBinding =
            DataBindingUtil.setContentView(this,R.layout.activity_dashboard)

        var MyReceiver: BroadcastReceiver?= null;
        MyReceiver = com.example.myrecharge.Helper.MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))



        val newFragment = Home_Fragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, newFragment, "home")
            .addToBackStack(null)
            .commitAllowingStateLoss()
        bottomNavigationbar()
        genrate_qr_code()

  
    }

    @SuppressLint("WrongConstant")
    fun bottomNavigationbar()
    {
// mainBinding.bottomNavigation.setItemIconTintList(R.color.Gold_color)
        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
//                    setFram(Home_Fragment())
                    
                    val newFragment = Home_Fragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, newFragment, "home")
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
//                    mainBinding.navigation.menu.findItem(android.R.id.navigation_home).isChecked = true

                    mainBinding.navigation.menu.findItem(R.id.navigation_home).isChecked = true
                    true
                }
                R.id.navigation_Wallet -> {
                    mainBinding.navigation.menu.findItem(R.id.navigation_Wallet).isChecked = true
                    setFram(WalletFragment())
// handle cli

                    true
                }
                R.id.navigation_profile -> {
                    mainBinding.navigation.menu.findItem(R.id.navigation_profile).isChecked = true
                    Log.e("@@naviagtionbar_log", "reward")
                    setFram(Profile_Fragment())
// handle clil
                    true
                }
                R.id.setting -> {
                    mainBinding.navigation.menu.findItem(R.id.setting).isChecked = true
                    Log.e("@@naviagtionbar_log", "reward")
                    setFram(Setting_fragment())
// handle clil
                    true
                }

            }
            false
        }
        mainBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    @SuppressLint("WrongConstant")
        fun setFram(fram: Fragment)
        {
    // initial transaction should be wrapped like this
            ;
            val newFragment = fram
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame, newFragment, "fragmente")
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("@@Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("@Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("@@Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }




    fun genrate_qr_code()
    {
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
//        mainBinding.iQrcode.setImageBitmap(bitmap)
    }

    fun exit_dialog()
    {
        val builder = AlertDialog.Builder(this,android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        //set title for alert dialog
        builder.setTitle("Exit")
        //set message for alert dialog
        builder.setMessage("Do you want to Exit.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
            builder.setPositiveButton("Yes"){dialogInterface, which ->
            Toast.makeText(applicationContext,"Exit....",Toast.LENGTH_LONG).show()
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
        }

        builder.setNegativeButton("No"){dialogInterface, which ->
            setFram(Home_Fragment())
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onBackPressed() {
        val fl: FrameLayout = findViewById(R.id.frame) as FrameLayout
        if (fl.getChildCount() === 1) {
            super.onBackPressed()
            if (fl.getChildCount() === 0) {



                // load your first Fragment here
                exit_dialog()
            }
        } else if (fl.getChildCount() === 0) {
            // load your first Fragment here
        } else {
            super.onBackPressed()
        }
    }
}