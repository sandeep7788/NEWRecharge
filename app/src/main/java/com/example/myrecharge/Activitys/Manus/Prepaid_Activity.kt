package com.example.myrecharge.Activitys.Manus

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Adapter.PaginationAdapter1
import com.example.myrecharge.Custom.CustomDialog
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.PaginationAdapterCallback
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.Models.OperatorModel
import com.example.myrecharge.Models.RechargereportModel
import com.example.myrecharge.Models.TopRatedMovies
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPrepaidBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class Prepaid_Activity : AppCompatActivity() , PaginationAdapterCallback {
    lateinit var mainBinding: ActivityPrepaidBinding
    var pref = Local_data(this@Prepaid_Activity)
    var TAG="@@prepaid"
    var mOption:Int=0
    var pausingDialog: SweetAlertDialog?=null
    var adapter: PaginationAdapter1? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var rechargeReportHistory:ArrayList<RechargereportModel> = java.util.ArrayList()
    var Last_History_ID=3093805
    var list_status:Boolean=false
    var viewAll_Status=true
    var Rechargetype="0"
    var last_history_ID=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_prepaid)
        pref.setMyappContext(this@Prepaid_Activity)

        linearLayoutManager = LinearLayoutManager(this@Prepaid_Activity, LinearLayoutManager.VERTICAL, false)
        mainBinding.mainRecycler!!.layoutManager = linearLayoutManager
        mainBinding.mainRecycler!!.itemAnimator = DefaultItemAnimator()
        adapter = PaginationAdapter1(this@Prepaid_Activity,rechargeReportHistory)
        mainBinding.mainRecycler.adapter=adapter
        mOption=intent.getIntExtra("opt",0)
        pausingDialog =SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
        pausingDialog!!.titleText = "Application waiting for internet connection..."
        pausingDialog!!.setCancelable(false)
        setView()
        monClick()
        getRechargetype()

        mainBinding.mainSwiperefresh.setOnRefreshListener {
            list_status=false
            adapter?.clear()
            rechargeReportHistory.clear()
            getRechargetype()
        }
    }

    fun setView(){
        when(mOption) {
            1 -> { mainBinding.imgNumber.setImageResource(R.drawable.prepaid)
                    Rechargetype= "Prepaidreport" }
            2 -> { mainBinding.imgNumber.setImageResource(R.drawable.prepaid)
                    Rechargetype= "Postpaidreport"}
            3 -> { mainBinding.imgNumber.setImageResource(R.drawable.dth)
                    Rechargetype= "Dthreport"}
            4 -> { mainBinding.imgNumber.setImageResource(R.drawable.electricity)
                    Rechargetype= "Datacardreport" }
            5 -> { mainBinding.imgNumber.setImageResource(R.drawable.moneytransfer)
                    Rechargetype= "Prepaidreport"}
            6 -> { mainBinding.imgNumber.setImageResource(R.drawable.bus)
                    Rechargetype= "Prepaidreport"}
            7 -> { mainBinding.imgNumber.setImageResource(R.drawable.flight)
                    Rechargetype= "Prepaidreport"}
            8 -> { mainBinding.imgNumber.setImageResource(R.drawable.pancard)
                    Rechargetype= "Datacardreport"}
            9 -> { mainBinding.imgNumber.setImageResource(R.drawable.landline)
                    Rechargetype= "Prepaidreport"}
            10 -> { mainBinding.imgNumber.setImageResource(R.drawable.gas)
                    Rechargetype= "Prepaidreport"
                    gasLayout() }
            11 -> { mainBinding.imgNumber.setImageResource(R.drawable.water)
                    Rechargetype= "Prepaidreport"}
            12 -> { mainBinding.imgNumber.setImageResource(R.drawable.hotel)
                    Rechargetype= "Prepaidreport"}
            else -> Toast.makeText(this@Prepaid_Activity,"Please go to back.... \n try again.",Toast.LENGTH_LONG).show()
        }
    }
    fun gasLayout() {
        mainBinding.prepaidAndSemilarLayout.visibility=View.GONE
        mainBinding.gasLayot.visibility=View.VISIBLE
    }

    fun monClick()
    {
        mainBinding.toolbarLayout.back.setOnClickListener {
            getRechargetype() //super.onBackPressed()
             }
        mainBinding.txtOperator.setOnClickListener { gerrateOperatorList("1") }
        mainBinding.txtViewAll.setOnClickListener { viewAll() }
    }

    fun getRechargetype()
    {
        val once = Once()
        var apiInterface: ApiInterface = RetrofitManager(this@Prepaid_Activity).instance!!.create(ApiInterface::class.java)

        apiInterface.getrechargereport("103225",last_history_ID.toString(),Rechargetype).enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@Prepaid_Activity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                list_status=false
                if(mainBinding.mainSwiperefresh.isRefreshing == true) {
                    mainBinding.mainSwiperefresh.setRefreshing(false) } else { }

                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.toString())
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    var operatorDialog : CustomDialog
                    Toast.makeText(this@Prepaid_Activity,mJsonObject.getString("Message")+" ",Toast.LENGTH_LONG).show()
                    var mJsonArray=mJsonObject.getJSONArray("Data")

                    Log.d(TAG, "size"+mJsonArray.length().toString()+"_"+Rechargetype)

                    if (mJsonObject.getString("Error").toLowerCase().equals("false")&&mJsonArray.length()>0){

                        mainBinding.imgEmptylist.visibility=View.GONE

                        for (i in 0 until mJsonArray.length()) {

                            var JsonObjectData=mJsonArray.getJSONObject(i)
                            var mRechargereportModel: RechargereportModel = RechargereportModel()
                            mRechargereportModel.historyID=JsonObjectData.getString("HistoryID").toInt()
                            mRechargereportModel.mobileno=JsonObjectData.getString("Mobileno")
                            mRechargereportModel.rechargeAmount=JsonObjectData.getString("RechargeAmount").toDouble()
                            mRechargereportModel.transID=JsonObjectData.getString("TransID")
                            mRechargereportModel.status=JsonObjectData.getString("Status")
                            mRechargereportModel.addDate=JsonObjectData.getString("AddDate")
                            mRechargereportModel.operatorName=JsonObjectData.getString("OperatorName")
                            mRechargereportModel.operaotimageurl=JsonObjectData.getString("operaotimageurl")
                            mRechargereportModel.rechargetime=JsonObjectData.getString("rechargetime")
                            rechargeReportHistory.add(mRechargereportModel)
                            Log.d("!!"+TAG, "data "+i.toString()+" "+mRechargereportModel.operatorName)

                            last_history_ID=JsonObjectData.getString("HistoryID").toInt()
                        }
                        mainBinding.mainProgress.visibility=View.GONE
                        adapter?.notifyDataSetChanged()

                        mainBinding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(
                                recyclerView: RecyclerView,
                                newState: Int
                            ) {
                                super.onScrollStateChanged(recyclerView, newState)
//                                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE)
                                if (recyclerView.canScrollVertically(1).not()
                                    && newState == RecyclerView.SCROLL_STATE_IDLE)
                                {

                                    Log.d(">>-----", "end")
                                    mainBinding.mainProgress.visibility=View.VISIBLE


                                    once.run(Runnable { getRechargetype()
                                        Log.d(">>-----", "end0") })

                                }
                            }
                        })
                    }
                    else {
                        mainBinding.imgEmptylist.visibility=View.VISIBLE
                        mainBinding.mainProgress.visibility=View.GONE
                    }
                }
            }
        })
    }

    fun gerrateOperatorList(operator_type:String)
    {
        pausingDialog?.show()
        var operatorList:ArrayList<OperatorModel> = java.util.ArrayList()
        var apiInterface: ApiInterface = RetrofitManager(this@Prepaid_Activity).instance!!.create(ApiInterface::class.java)

        apiInterface.getoperatordetails(operator_type).enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@Prepaid_Activity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    var operatorDialog : CustomDialog
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
                        operatorDialog=
                            CustomDialog(
                                this@Prepaid_Activity,
                                operatorList,
                                mainBinding.txtOperator
                            )
                        operatorDialog.getWindow()!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        operatorDialog.show()
                    }
                }
                pausingDialog?.dismiss()
            }
        })
    }

    override fun retryPageLoad() {
//        TODO("Not yet implemented")
    }
    fun viewAll() {
        if(viewAll_Status) {
            mainBinding.topCardview.visibility=View.GONE
            mainBinding.txtViewAll.text="Re-size"
            viewAll_Status=false
        }
        else {
            mainBinding.topCardview.visibility = View.VISIBLE
            mainBinding.txtViewAll.text = "View All"
            viewAll_Status = true
        }
    }
}

class Once {
    private val done: AtomicBoolean = AtomicBoolean()
    fun run(task: Runnable) {
        if (done.get()) return
        if (done.compareAndSet(false, true)) {
            task.run()
        }
    }
}