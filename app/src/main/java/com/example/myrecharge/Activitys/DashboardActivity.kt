package com.example.myrecharge.Activitys

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.fragment.app.FragmentTransaction
import com.example.myrecharge.Fragment.Home_Fragment
import com.example.myrecharge.Fragment.Profile_Fragment
import com.example.myrecharge.Fragment.Setting_fragment
import com.example.myrecharge.Fragment.WalletFragment
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class DashboardActivity : AppCompatActivity() {

    lateinit var mainBinding : ActivityDashboardBinding
    lateinit var transaction:FragmentTransaction
    var pref= Local_data(this@DashboardActivity)
    var  CAMERA_PERMISSION_CODE = 100
    var  STORAGE_PERMISSION_CODE = 101
    var FRAGMENT_OTHER="FRAGMENT_OTHER"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        pref.setMyappContext(this@DashboardActivity)
        mainBinding =
            DataBindingUtil.setContentView(this,R.layout.activity_dashboard)

        var MyReceiver: BroadcastReceiver?= null;
        MyReceiver = MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        setFram(Home_Fragment())

        mainBinding.navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewFragment(Home_Fragment(), "FRAGMENT_HOME")
                }
                R.id.navigation_Wallet -> {
                    viewFragment(WalletFragment(), FRAGMENT_OTHER)
                }
                R.id.navigation_profile -> {
                    viewFragment(Profile_Fragment(), FRAGMENT_OTHER)
                }
                R.id.setting -> {
                    viewFragment(Setting_fragment(), FRAGMENT_OTHER)
                }
            }
            true
        })
        genrate_qr_code()
        checkPermission(arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),CAMERA_PERMISSION_CODE)
    }

    @SuppressLint("WrongConstant")
        fun setFram(fram: Fragment)
        {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction =
                fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame,fram)
            fragmentTransaction.commit()
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

    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun viewFragment(
        fragment: Fragment,
        name: String
    ) {
        var fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        // 1. Know how many fragments there are in the stack
        val count = fragmentManager.backStackEntryCount
        // 2. If the fragment is **not** "home type", save it to the stack
        if (name == FRAGMENT_OTHER) {
            fragmentTransaction.addToBackStack(name)
        }
        // Commit !
        fragmentTransaction.commit()
        // 3. After the commit, if the fragment is not an "home type" the back stack is changed, triggering the
        // OnBackStackChanged callback
        fragmentManager.addOnBackStackChangedListener(object :
            FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                // If the stack decreases it means I clicked the back button
                if (fragmentManager.backStackEntryCount <= count) {
                    // pop all the fragment and remove the listener
                    fragmentManager.popBackStack(FRAGMENT_OTHER, POP_BACK_STACK_INCLUSIVE)
                    fragmentManager.removeOnBackStackChangedListener(this)
                    // set the home button selected
                    mainBinding.navigation.getMenu().getItem(0).setChecked(true)
                }
            }
        })
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
    fun checkPermission(permission: Array<out String>, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@DashboardActivity, permission[0]) === PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@DashboardActivity, permission,
                requestCode
            )
        } else {
            Toast.makeText(
                this@DashboardActivity,
                "Permission already granted",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
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
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,
                    "Storage Permission Granted",
                    Toast.LENGTH_SHORT)
                    .show();

            }
            else {
                Toast.makeText(this,
                    "Storage Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }
}