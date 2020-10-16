package com.example.myrecharge.Activitys.Manus

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Activitys.ValueFilter
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Method_collection
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.Models.OperatorModel
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPrepaidBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Prepaid_Activity : AppCompatActivity() {
    lateinit var mainBinding: ActivityPrepaidBinding
    var methodCollection=Method_collection(this@Prepaid_Activity)
    var pref = Local_data(this@Prepaid_Activity)
    var TAG="@@prepaid"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding =
            DataBindingUtil.setContentView(this,R.layout.activity_prepaid)
        pref.setMyappContext(this@Prepaid_Activity)
        methodCollection!!.NetworkServices()
        monClick()
    }

    fun monClick()
    {
        mainBinding.toolbar.back.setOnClickListener { super.onBackPressed() }
    }
    fun gerrateOperatorList()
    {
        var operatorList:ArrayList<OperatorModel> = java.util.ArrayList()
        var apiInterface: ApiInterface = RetrofitManager.instance!!.create(ApiInterface::class.java)

        apiInterface.getoperatordetails("1").enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@Prepaid_Activity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    var operatorDialog : Demo
                    Toast.makeText(this@Prepaid_Activity,mJsonObject.getString("Message")+" ",Toast.LENGTH_LONG).show()
                    if (mJsonObject.getString("Error").toLowerCase().equals("false")){
                        var mJsonArray=mJsonObject.getJSONArray("Data")

                        operatorList.clear()

                        for (i in 0 until mJsonArray.length()){

                            var JsonObjectData=mJsonArray.getJSONObject(i)
                            var mOperatorModel: OperatorModel = OperatorModel()
                            mOperatorModel.operatorID=JsonObjectData.getString("OperatorID").toInt()
                            mOperatorModel.operatorName=JsonObjectData.getString("OperatorName")
                            mOperatorModel.operatorCode=JsonObjectData.getString("OperatorCode")
                            mOperatorModel.operaotimageurl=JsonObjectData.getString("operaotimageurl")
                            operatorList.add(mOperatorModel) }
                        operatorDialog= Demo(this@Prepaid_Activity,operatorList,mainBinding.txtOperator)
                        operatorDialog.getWindow()!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        operatorDialog.show()
                    }
                }
            }
        })
    }
}