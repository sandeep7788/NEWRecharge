package com.example.myrecharge.Activitys

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myrecharge.Helper.*
import com.example.myrecharge.R
import com.google.gson.JsonObject
import com.pnikosis.materialishprogress.ProgressWheel
import de.mateware.snacky.Snacky
import org.json.JSONArray
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SplachScreen : AppCompatActivity(), Observer {
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    var f39t: TextView? = null
    var t1: TextView? = null
    var version: String? = null
    var mLocal_data= Local_data(this@SplachScreen)
    var mdevice:String="0000"
    lateinit var gifview:GifImageView
    lateinit var i_re_try:ImageView
    lateinit var pDialog: ProgressWheel
    val TAG="@@splachscreen"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach_screen)
        mLocal_data.setMyappContext(this@SplachScreen)
        i_re_try=findViewById(R.id.i_re_try)
        pDialog=findViewById(R.id.progress_bar)
        var MyReceiver: BroadcastReceiver? = null;
        MyReceiver = com.example.myrecharge.Helper.MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

//        isOnline(this)
        Log.d(TAG, isOnline(this).toString())
/*
        if(isOnline(this@SplachScreen)){
            login()
        }
*/

}

    fun login()
    {
        pDialog!!.visibility=View.VISIBLE
        var apiInterface: ApiInterface = RetrofitManager.instance!!.create(ApiInterface::class.java)

        apiInterface.getLogin(mLocal_data.ReadStringPreferences(Constances.PREF_Mobile),mLocal_data.ReadStringPreferences(Constances.PREF_Login_password)).
        enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@SplachScreen," "+t.message.toString(),Toast.LENGTH_LONG).show()
                pDialog!!.visibility=View.GONE
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {

                    Log.d(TAG, "onResponse: "+response.body().toString())
                    val jsonObject= JSONObject(response.body().toString())

                    if(jsonObject.getString("Error").toLowerCase().equals("false")){
                        Toast.makeText(this@SplachScreen," "+jsonObject.getString("Message"),Toast.LENGTH_LONG).show()

                        val json_Array: JSONArray =jsonObject.getJSONArray("Data")
                        for (i in 0 until json_Array.length()){

                            val jsonobject1:JSONObject = json_Array.getJSONObject(i)
                            mLocal_data.writeStringPreference(Constances.PREF_Msrno,jsonobject1.getString("Msrno"))
                            mLocal_data.writeStringPreference(Constances.PREF_Membertype,jsonobject1.getString("Membertype"))
                            mLocal_data.writeStringPreference(Constances.PREF_MemberID,jsonobject1.getString("MemberID"))
                            mLocal_data.writeStringPreference(Constances.PREF_MemberName,jsonobject1.getString("MemberName"))
                            mLocal_data.writeStringPreference(Constances.PREF_Mobile,jsonobject1.getString("Mobile"))
                            mLocal_data.writeStringPreference(Constances.PREF_TransPass,jsonobject1.getString("TransPass"))
                            pDialog!!.visibility=View.GONE
                            Log.d(TAG, "onResponse: 1"+jsonobject1.getString("Msrno"))
                            Log.d(TAG, "onResponse: 2"+jsonobject1.getString("Membertype"))
                            startActivity(Intent(this@SplachScreen,DashboardActivity::class.java))
                        }
                    }
                    else{
                        pDialog!!.visibility=View.GONE
                        Toast.makeText(this@SplachScreen,"Bad Response ! ",Toast.LENGTH_LONG).show()
                    }
                }else{
                    pDialog!!.visibility=View.GONE
                    Toast.makeText(this@SplachScreen,"Bad Response ! ",Toast.LENGTH_LONG).show()
                }

            }

        })
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

    override fun update(p0: Observable?, p1: Any?) {
        ObservableObject.getInstance().updateValue(intent)
        Log.d(TAG, "update: >>")
    }

}