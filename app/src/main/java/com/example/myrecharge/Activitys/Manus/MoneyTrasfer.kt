package com.example.myrecharge.Activitys.Manus

import android.app.Activity
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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Adapter.BankListAdapter
import com.example.myrecharge.Custom_.CustomAlertAdapter
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.Models.BankListModel
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityMoneyTrasferBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoneyTrasfer : AppCompatActivity() {
    lateinit var mainBinding: ActivityMoneyTrasferBinding
    var pref = Local_data(this@MoneyTrasfer)
    var TAG="@@monettransfer"
    var linearLayoutManager: LinearLayoutManager? = null
    var rechargeReportHistory:ArrayList<BankListModel> = java.util.ArrayList()
    var array_sort = java.util.ArrayList<BankListModel>()
    var adapter: BankListAdapter? = null
    var pausingDialog: SweetAlertDialog?=null
    var textlength = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_money_trasfer)
        pref.setMyappContext(this@MoneyTrasfer)

        linearLayoutManager = LinearLayoutManager(this@MoneyTrasfer, LinearLayoutManager.VERTICAL, false)
        mainBinding.mainRecycler!!.layoutManager = linearLayoutManager
        mainBinding.mainRecycler!!.itemAnimator = DefaultItemAnimator()
        adapter = BankListAdapter(rechargeReportHistory,this@MoneyTrasfer)
        mainBinding.mainRecycler.adapter=adapter
        var MyReceiver: BroadcastReceiver?= null;
        MyReceiver = com.example.myrecharge.Helper.MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        pausingDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pausingDialog!!.titleText = "Please wait...."
        pausingDialog!!.setCancelable(false)
        getBankList()
        list_filter()

        mainBinding.mainSwiperefresh.setOnRefreshListener {

            getBankList()
        }
    }
    fun getBankList()
    {
        mainBinding.imgEmptylist.visibility = View.GONE
        mainBinding.mainProgress.visibility = View.VISIBLE
        var apiInterface: ApiInterface = RetrofitManager(this@MoneyTrasfer).instance!!.create(
            ApiInterface::class.java)

        apiInterface.getAllbanknamedetails().
        enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@MoneyTrasfer,t.message.toString()+" ", Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
                mainBinding.imgEmptylist.visibility = View.VISIBLE
                mainBinding.mainProgress.visibility = View.GONE
                if(mainBinding.mainSwiperefresh.isRefreshing == true) {
                    mainBinding.mainSwiperefresh.setRefreshing(false) } else { }
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(mainBinding.mainSwiperefresh.isRefreshing == true) {
                    mainBinding.mainSwiperefresh.setRefreshing(false) } else { }
                if(mainBinding.mainSwiperefresh.isRefreshing == true) {
                    mainBinding.mainSwiperefresh.setRefreshing(false) } else { }

                mainBinding.imgEmptylist.visibility = View.VISIBLE
                mainBinding.mainProgress.visibility = View.GONE

                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.toString())
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
//                    Toast.makeText(this@Prepaid_Activity,mJsonObject.getString("Message")+" ",Toast.LENGTH_LONG).show()

                    if(mJsonObject.has("Data") && !mJsonObject.isNull("Data") ) {

                        var mJsonArray = mJsonObject.getJSONArray("Data")

                        if (mJsonObject.getString("Error").toLowerCase()
                                .equals("false") && mJsonArray != null
                        ) {
                            rechargeReportHistory.clear()
                            array_sort.clear()
                            for (i in 0 until mJsonArray.length()) {

                                var JsonObjectData = mJsonArray.getJSONObject(i)
                                var mRechargereportModel: BankListModel = BankListModel()
                                mRechargereportModel.bankerMasterID =
                                    JsonObjectData.getString("BankerMasterID").toInt()
                                mRechargereportModel.bankerMasterName =
                                    JsonObjectData.getString("BankerMasterName")

                                rechargeReportHistory.add(mRechargereportModel)
                                Log.d("!!" + TAG, "data " + i.toString() + " " + mRechargereportModel.bankerMasterName)

                            }
                            adapter?.notifyDataSetChanged()

                        } else {
                            mainBinding.imgEmptylist.visibility = View.VISIBLE
                            mainBinding.mainProgress.visibility = View.GONE
                            
                        }
                    } else {
                        mainBinding.imgEmptylist.visibility = View.VISIBLE
                        mainBinding.mainProgress.visibility = View.GONE
                        
                    }
                } else {
                    mainBinding.imgEmptylist.visibility = View.VISIBLE
                    mainBinding.mainProgress.visibility = View.GONE
                    
                }
            }
        })
    }

    fun list_filter() {
        mainBinding.edtSearche.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                mainBinding.edtSearche.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                textlength = mainBinding.edtSearche.getText().length
                array_sort.clear()
                for (i in rechargeReportHistory.indices) {
                    if (textlength <= rechargeReportHistory.get(i).bankerMasterName.length) {
                        if (rechargeReportHistory.get(i).bankerMasterName.toLowerCase().contains(
                                mainBinding.edtSearche.getText().toString().toLowerCase()
                                    .trim({ it <= ' ' })
                            )
                        ) {
                            array_sort.add(rechargeReportHistory.get(i))
                        }
                    }
                }
                mainBinding.mainRecycler.setAdapter(
                    BankListAdapter(
                        array_sort,this@MoneyTrasfer
                    )
                )
            }
        })
    }
}