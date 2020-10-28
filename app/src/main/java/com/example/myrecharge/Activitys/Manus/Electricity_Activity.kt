package com.example.myrecharge.Activitys.Manus

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Adapter.PassbookAdapter
import com.example.myrecharge.Custom_.CustomDialog
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.Models.HistoryModel
import com.example.myrecharge.Models.OperatorModel
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityElectricityBinding
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_electricity_.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Electricity_Activity : AppCompatActivity() {
    lateinit var mainBinding: ActivityElectricityBinding
    var pref = Local_data(this@Electricity_Activity)
    var TAG="@@electricity"
    var pausingDialog: SweetAlertDialog?=null
    var adapter: PassbookAdapter? = null
    var history_list:ArrayList<HistoryModel> = java.util.ArrayList()
    var Last_History_ID=0
    var linearLayoutManager: LinearLayoutManager? = null
    var mOperator_CODE="101"
    var tel_no="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_electricity_)
        var MyReceiver: BroadcastReceiver?= null;
        MyReceiver = com.example.myrecharge.Helper.MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        monClick()
        setview()

        mainBinding.txtBoard.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                get_billGetFatchbilleroperatordetails()
            }
        })
    }

    fun monClick()
    {
        mainBinding.toolbarLayout.back.setOnClickListener {
            super.onBackPressed()
        }
        mainBinding.txtBoard.setOnClickListener {
            getoperatorlist("7")
        }
        mainBinding.btnFatchBill.setOnClickListener {
            if(mainBinding.txtBoard.text.isEmpty()) {
                Toast.makeText(
                    this@Electricity_Activity,"Please Select Board",Toast.LENGTH_LONG).show()
                mainBinding.txtBoard.setError("Please Select Board")
            } else if(mainBinding.edtBillNumber.text.isEmpty()) {
                Toast.makeText(
                    this@Electricity_Activity,"Please Enter Bill number",Toast.LENGTH_LONG).show()
                mainBinding.edtBillNumber.setError("Please Enter Bill number")
            }
            else {
                mainBinding.txtBoard.setError(null)
                mainBinding.edtBillNumber.setError(null)
                fatch_Bill() }
        }
        mainBinding.btnProcess.setOnClickListener {
            if(mainBinding.txtBoard.text.isEmpty()) {
                Toast.makeText(
                    this@Electricity_Activity,"Please Select Board",Toast.LENGTH_LONG).show()
            } else if(mainBinding.edtBillNumber.text.isEmpty()) {
                Toast.makeText(
                    this@Electricity_Activity,"Please Enter Bill number",Toast.LENGTH_LONG).show()
                mainBinding.edtBillNumber.setError("Please Enter Bill number")
            } else if(mainBinding.edtNumber.text!!.isEmpty()&&mainBinding.edtNumber.text!!.length<9) {
                Toast.makeText(
                    this@Electricity_Activity,"Please Enter Mobile Number",Toast.LENGTH_LONG).show()
            } else if(mainBinding.edtAmount.text!!.isEmpty()) {
                Toast.makeText(
                    this@Electricity_Activity,"Please Enter Amount",Toast.LENGTH_LONG).show()
            }
            else {
                mainBinding.edtBillNumber.setError(null)
                mainBinding.edtNumber.setError(null)
                mainBinding.edtAmount.setError(null)
      //
                }
        }
    }

    fun setview() {
        mainBinding.mainProgress.visibility=View.GONE
    }

    fun fatch_Bill() {
        pausingDialog?.show()
        Log.e(TAG,"caled1"+pref.ReadStringPreferences(Constances.PREF_operator_code)+"_"+tel_no.toString());
        var apiInterface: ApiInterface = RetrofitManager(this@Electricity_Activity).instance!!.create(
            ApiInterface::class.java)
        apiInterface.billGetFatchdetails(pref.ReadStringPreferences(Constances.PREF_operator_code),tel_no).enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@Electricity_Activity,t.message.toString()+" ", Toast.LENGTH_LONG).show()
                    pausingDialog?.dismiss()
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pausingDialog?.dismiss()
                    if(response.isSuccessful) {
                        Log.d(TAG, "billGetFatchdetails" + response.body().toString())
                        var mJsonObject = JSONObject(response.body().toString())
                        Toast.makeText(
                            this@Electricity_Activity, mJsonObject.getString("Message") + " ",Toast.LENGTH_LONG).show()
                        if (mJsonObject.getString("Error").toLowerCase().equals("false")) {

                                var mJsonArray = mJsonObject.getJSONObject("Data")

//                            mJsonArray.getString("operator")

                                if(mJsonObject.has("records") && !mJsonObject.isNull("records") ) {
                                    var mRecords = mJsonArray.getJSONArray("records")
                                    var mRecordsObject = mRecords.getJSONObject(0)

                                    if(mRecordsObject.getString("CustomerName")!=null&&mRecordsObject.getString("CustomerName")!=null&&
                                        mRecordsObject.getString("CustomerName")!=null&&mRecordsObject.getString("CustomerName")!=null)
                                    mainBinding.cName.text=mRecordsObject.getString("CustomerName")
                                    mainBinding.cBillDate.text=mRecordsObject.getString("Billamount")
                                    mainBinding.cDueDate.text=mRecordsObject.getString("Billdate")
                                    mainBinding.cAmount.text=mRecordsObject.getString("Duedate")
                                    mainBinding.imgFachbill.visibility=View.GONE
                                    mainBinding.layoutShoewFachDetails.visibility=View.VISIBLE

                                }
                                else {
                                    mainBinding.imgFachbill.visibility=View.VISIBLE
                                    mainBinding.layoutShoewFachDetails.visibility=View.GONE
                                }
                        }
                    }
                }
            })
    }

    fun get_billGetFatchbilleroperatordetails() {
        pausingDialog?.show()
        var operatorList:ArrayList<OperatorModel> = java.util.ArrayList()
        var apiInterface: ApiInterface = RetrofitManager(this@Electricity_Activity).instance!!.create(
            ApiInterface::class.java)

        Log.d(TAG, "get_billGetFatchbilleroperatordetails: "+pref.ReadStringPreferences(Constances.PREF_Mobile)+"\n"
        +pref.ReadStringPreferences(Constances.PREF_Login_password)+"\n "+
                pref.ReadStringPreferences(Constances.PREF_operator_code))
        apiInterface.get_billGetFatchbilleroperatordetails(pref.ReadStringPreferences(Constances.PREF_Mobile),pref.ReadStringPreferences(Constances.PREF_Login_password),pref.ReadStringPreferences(Constances.PREF_operator_code)).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@Electricity_Activity,t.message.toString()+" ", Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                pausingDialog?.dismiss()
                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: 1" + response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    Toast.makeText(
                        this@Electricity_Activity, mJsonObject.getString("Message") + " ",
                        Toast.LENGTH_LONG
                    ).show()
                    if (mJsonObject.getString("Error").toLowerCase().equals("false")) {
                        var mJsonArray = mJsonObject.getJSONArray("Data")
                        var JsonObjectData=mJsonArray.getJSONObject(0)

                        /*JsonObjectData.getString("OperatorName")
                        JsonObjectData.getString("operatorcode")
                        JsonObjectData.getString("Numbertype")
                        JsonObjectData.getString("Extranumber")
                        JsonObjectData.getString("digit")*/
                        tel_no=JsonObjectData.getString("Numbertype");
                        show_fachbill_layout(JsonObjectData.getString("Numbertype")+" ")
                    }
                }
            }

        })
    }

    fun getoperatorlist(operator_type:String)
    {
        pausingDialog?.show()
        var operatorList:ArrayList<OperatorModel> = java.util.ArrayList()
        var apiInterface: ApiInterface = RetrofitManager(this@Electricity_Activity).instance!!.create(
            ApiInterface::class.java)

        apiInterface.getoperatordetails(operator_type).enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@Electricity_Activity,t.message.toString()+" ", Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                pausingDialog?.dismiss()
                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    var operatorDialog : CustomDialog
                    Toast.makeText(this@Electricity_Activity,mJsonObject.getString("Message")+" ",
                        Toast.LENGTH_LONG).show()
                    if (mJsonObject.getString("Error").toLowerCase().equals("false")){
                        var mJsonArray=mJsonObject.getJSONArray("Data")

                        operatorList.clear()

                        for (i in 0 until mJsonArray.length()) {
                            var JsonObjectData=mJsonArray.getJSONObject(i)
                            var mOperatorModel: OperatorModel = OperatorModel()
                            mOperatorModel.operatorID=JsonObjectData.getString("OperatorID").toInt()
                            mOperatorModel.operatorName=JsonObjectData.getString("OperatorName")
                            mOperatorModel.operatorCode=JsonObjectData.getString("OperatorCode")
                            mOperatorModel.operaotimageurl=JsonObjectData.getString("operaotimageurl")
                            operatorList.add(mOperatorModel) }

                        operatorDialog=
                            CustomDialog(
                                this@Electricity_Activity,
                                operatorList,
                                mainBinding.txtBoard,
                                mOperator_CODE
                            )
                        operatorDialog.getWindow()!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        operatorDialog.show()
                    }
                }

            }
        })
    }

    fun show_fachbill_layout(hint:String) {
        mainBinding.layoutFachBill.visibility=View.VISIBLE
        mainBinding.layoutFachBill.edt_bill_number.setHint(hint)
    }
    override fun onResume() {
        Log.d(TAG, "onResume: "+mOperator_CODE)
        super.onResume()

    }

    override fun onBackPressed() {
        cln()
        super.onBackPressed()
    }

    fun cln() {
        pref.writeStringPreference(Constances.PREF_amt,"")
        pref.writeStringPreference(Constances.PREF_operator_code,"")
        pref.writeStringPreference(Constances.PREF_temp_number,"")
        pref.writeIntPreference("opt",0)
    }
}