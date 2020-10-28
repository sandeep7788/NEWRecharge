package com.example.myrecharge.Activitys.Manus

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Adapter.PassbookAdapter
import com.example.myrecharge.Custom_.CustomDialog
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.Models.HistoryModel
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityHistoryBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class History_Activity : AppCompatActivity() {
    lateinit var mainBinding: ActivityHistoryBinding
    var pref = Local_data(this@History_Activity)
    var TAG="@@prepaid"
    var pausingDialog: SweetAlertDialog?=null
    var adapter: PassbookAdapter? = null
    var history_list:ArrayList<HistoryModel> = java.util.ArrayList()
    var Last_History_ID=0
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_history_)
        pref.setMyappContext(this@History_Activity)
        pausingDialog =SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
        pausingDialog!!.titleText = "Application waiting for internet connection..."
        pausingDialog!!.setCancelable(false)

        linearLayoutManager = LinearLayoutManager(this@History_Activity, LinearLayoutManager.VERTICAL, false)
        mainBinding.mainRecycler!!.layoutManager = linearLayoutManager
        mainBinding.mainRecycler!!.itemAnimator = DefaultItemAnimator()
        adapter = PassbookAdapter(this@History_Activity,history_list)
        mainBinding.mainRecycler.adapter=adapter

        getHistory()
        monClick()

        mainBinding.mainSwiperefresh.setOnRefreshListener {
            adapter?.clear()
            history_list.clear()
            Last_History_ID=0
            getHistory()
        }
    }

    fun monClick()
    {
        mainBinding.toolbarLayout.back.setOnClickListener {
            super.onBackPressed()
        }
    }

    fun getHistory()
    {
        mainBinding.mainProgress.visibility=View.VISIBLE
        val once = Once()
        var apiInterface: ApiInterface = RetrofitManager(this).instance!!.create(
            ApiInterface::class.java)

        apiInterface.getHistory(pref.ReadStringPreferences(Constances.PREF_Msrno),Last_History_ID.toString()," ").enqueue(object :
            Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@History_Activity,t.message.toString()+" ", Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                stop_progress()

                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.toString())
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    var operatorDialog : CustomDialog
                    Toast.makeText(this@History_Activity,mJsonObject.getString("Message")+" ",
                        Toast.LENGTH_LONG).show()

                    if(mJsonObject.has("Data") && !mJsonObject.isNull("Data") ) {

                        var mJsonArray = mJsonObject.getJSONArray("Data")

                        if (mJsonObject.getString("Error").toLowerCase()
                                .equals("false") && mJsonArray != null
                        ) {

                            for (i in 0 until mJsonArray.length()) {

                                var JsonObjectData = mJsonArray.getJSONObject(i)
                                var mHistoryModel: HistoryModel = HistoryModel()
                                mHistoryModel.eWalletTransactionID =
                                    JsonObjectData.getString("EWalletTransactionID").toInt()
                                mHistoryModel.amount = JsonObjectData.getString("Amount").toDouble()
                                mHistoryModel.balance =
                                    JsonObjectData.getString("Balance").toDouble()
                                mHistoryModel.narration = JsonObjectData.getString("Narration")
                                mHistoryModel.adddate = JsonObjectData.getString("Adddate")
                                mHistoryModel.factor = JsonObjectData.getString("Factor")
                                mHistoryModel.wallettime = JsonObjectData.getString("Wallettime")
                                history_list.add(mHistoryModel)
                                Last_History_ID = JsonObjectData.getString("EWalletTransactionID").toInt()
                            }
                            stop_progress()
                            adapter?.notifyDataSetChanged()

                            mainBinding.mainRecycler.addOnScrollListener(object :
                                RecyclerView.OnScrollListener() {
                                override fun onScrollStateChanged(
                                    recyclerView: RecyclerView,
                                    newState: Int
                                ) {
                                    super.onScrollStateChanged(recyclerView, newState)
//                                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE)
                                    if (recyclerView.canScrollVertically(1).not()
                                        && newState == RecyclerView.SCROLL_STATE_IDLE
                                    ) {

                                        Log.d(">>-----", "end")



                                        once.run(Runnable {
                                            getHistory()
                                            Log.d(">>-----", "end0")
                                        })

                                    }
                                }
                            })
                        }
                    }
                    else {
                        stop_progress()
                    }
                }
                else {
                    stop_progress()
                }
            }
        })
    }
    fun stop_progress() {
        mainBinding.imgEmptylist.visibility= View.VISIBLE
        mainBinding.mainProgress.visibility= View.GONE
        if(mainBinding.mainSwiperefresh.isRefreshing == true) {
            mainBinding.mainSwiperefresh.setRefreshing(false) } else { }
    }
}