package com.example.myrecharge.Activitys

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityLoginBinding
import com.google.gson.JsonObject
import de.mateware.snacky.Snacky
import dmax.dialog.SpotsDialog
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login_Activity: AppCompatActivity() {

    lateinit var mainBinding :ActivityLoginBinding
    lateinit var transaction: FragmentTransaction
    lateinit var mLocal_data: Local_data
    var pref= Local_data(this@Login_Activity)
    var TAG="@@login"
    var pDialog:SweetAlertDialog ?=null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        mLocal_data= Local_data(this)

        pDialog=SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#132752")
        pDialog!!.titleText = "Loading ..."
        pDialog!!.setCancelable(false)

        var MyReceiver: BroadcastReceiver? = null;
        MyReceiver = com.example.myrecharge.Helper.MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))



        mainBinding.btnSignIn.setOnClickListener {

            if(mainBinding.edtLoginID.text.isEmpty())
            {
                mainBinding.edtLoginID.setError("Please Enter Login-ID")
            }
            else if(mainBinding.edtPassword.text.isEmpty())
            {
                mainBinding.edtPassword.setError("Please Enter Password")
            }
            else
            {
                login()

            }
        }
    }


    fun login()
    {
        pDialog!!.show()
        var apiInterface:ApiInterface=RetrofitManager.instance!!.create(ApiInterface::class.java)

        apiInterface.getLogin(mainBinding.edtLoginID.text.toString(),mainBinding.edtPassword.text.toString()).
    enqueue(object :Callback<JsonObject>
    {
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            Toast.makeText(this@Login_Activity," "+t.message.toString(),Toast.LENGTH_LONG).show()
            pDialog!!.dismiss()
        }

        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
            if(response.isSuccessful) {

                Log.d(TAG, "onResponse: "+response.body().toString())
                val jsonObject= JSONObject(response.body().toString())

                if(jsonObject.getString("Error").toLowerCase().equals("false")){
                    Toast.makeText(this@Login_Activity," "+jsonObject.getString("Message"),Toast.LENGTH_LONG).show()

                    val json_Array:JSONArray=jsonObject.getJSONArray("Data")
                    for (i in 0 until json_Array.length()){

                        val jsonobject1:JSONObject = json_Array.getJSONObject(i)
                        mLocal_data.writeStringPreference(Constances.PREF_Login_password,mainBinding.edtPassword.text.toString())
                        mLocal_data.writeStringPreference(Constances.PREF_Msrno,jsonobject1.getString("Msrno"))
                        mLocal_data.writeStringPreference(Constances.PREF_Membertype,jsonobject1.getString("Membertype"))
                        mLocal_data.writeStringPreference(Constances.PREF_MemberID,jsonobject1.getString("MemberID"))
                        mLocal_data.writeStringPreference(Constances.PREF_MemberName,jsonobject1.getString("MemberName"))
                        mLocal_data.writeStringPreference(Constances.PREF_Mobile,jsonobject1.getString("Mobile"))
                        mLocal_data.writeStringPreference(Constances.PREF_TransPass,jsonobject1.getString("TransPass"))
                        pDialog!!.dismiss()
                        Log.d(TAG, "onResponse: 1"+jsonobject1.getString("Msrno"))
                        Log.d(TAG, "onResponse: 2"+jsonobject1.getString("Membertype"))
                        startActivity(Intent(this@Login_Activity,DashboardActivity::class.java))
                    }
                }
                else{
                    pDialog!!.dismiss()
                    Toast.makeText(this@Login_Activity,"Bad Response ! ",Toast.LENGTH_LONG).show()
                }
            }else{
                pDialog!!.dismiss()
                Toast.makeText(this@Login_Activity,"Bad Response ! ",Toast.LENGTH_LONG).show()
            }

        }

    })
    }
}