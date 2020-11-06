package com.example.myrecharge.Activitys.Manus

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Activitys.DashboardActivity
import com.example.myrecharge.Adapter.DynamicTabFragmentPagerAdapter
import com.example.myrecharge.Adapter.Pay_Adapter1
import com.example.myrecharge.Adapter.TopServicesAdapter
import com.example.myrecharge.Custom_.CustomDialog
import com.example.myrecharge.Helper.*
import com.example.myrecharge.Models.OperatorModel
import com.example.myrecharge.Models.RechargePlaneModel
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPrepaidBinding
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class TopServicesActivity : AppCompatActivity() , PaginationAdapterCallback {
    lateinit var mainBinding: ActivityPrepaidBinding
    var pref = Local_data(this@TopServicesActivity)
    var TAG="@@prepaid"
    var mOption:Int=0
    var pausingDialog: SweetAlertDialog?=null
    var adapter: Pay_Adapter1? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var rechargeReportHistory:ArrayList<RechargePlaneModel> = java.util.ArrayList()
    var viewAll_Status=true
    var Rechargetype="0"
    var last_history_ID=0
    var mOperator_CODE="101"
    var operator_type="1"
    val once = Once()
    var isLoading=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_prepaid)
        pref.setMyappContext(this@TopServicesActivity)

        linearLayoutManager = LinearLayoutManager(this@TopServicesActivity, LinearLayoutManager.VERTICAL, false)
        mainBinding.mainRecycler!!.layoutManager = linearLayoutManager
        mainBinding.mainRecycler!!.itemAnimator = DefaultItemAnimator()
        adapter = Pay_Adapter1(
            this@TopServicesActivity,
            rechargeReportHistory
        )
        mainBinding.mainRecycler.adapter=adapter
        var MyReceiver: BroadcastReceiver?= null;
        MyReceiver = MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        mOption=pref.ReadIntPreferences("opt")
        pausingDialog =SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE)
        pausingDialog!!.titleText = "Please wait...."
        pausingDialog!!.setCancelable(false)
        setView()
        monClick()
        getrechargereport()

        mainBinding.mainSwiperefresh.setOnRefreshListener {
            adapter?.clear()
            rechargeReportHistory.clear()
            last_history_ID=0
            getrechargereport()
        }

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
                    if(!isLoading){
                        isLoading=true
                        getrechargereport()
                        Log.d(">>-----", "end1")

                    }

                }
            }
        })


    }

    fun setView() {
        Log.d(TAG, "setview1 "+pref.ReadStringPreferences(Constances.PREF_amt))
        Log.d(TAG, "setview2 "+pref.ReadStringPreferences(Constances.PREF_operator_code))
        Log.d(TAG, "setview3 "+pref.ReadIntPreferences("opt"))

        if(pref.ReadStringPreferences(Constances.PREF_amt).length>2) {
            var mSelectedplane = pref.ReadStringPreferences(Constances.PREF_amt).replace("₹","");
            mainBinding.txtSelectedRechargePlane.setText(mSelectedplane)
            mainBinding.txtSelectedRechargePlane.setTextColor(Color.BLACK)
            mainBinding.txtOperator.text=pref.ReadStringPreferences(Constances.PREF_operator_code)
            mainBinding.edtNumber.setText(pref.ReadStringPreferences(Constances.PREF_temp_number))
            mainBinding.txtOperator.setTextColor(Color.BLACK)
        }

        when(mOption) {
            1 -> { mainBinding.imgNumber.setImageResource(R.drawable.prepaid)
                    Rechargetype= "Prepaidreport"
                operator_type="1"
            }
            2 -> { mainBinding.imgNumber.setImageResource(R.drawable.prepaid)
                    Rechargetype= "Postpaidreport"
                operator_type="2" }
            3 -> { mainBinding.imgNumber.setImageResource(R.drawable.dth)
                    Rechargetype= "Dthreport"
                operator_type="5" }
            4 -> { mainBinding.imgNumber.setImageResource(R.drawable.electricity)
                    Rechargetype= "Datacardreport" }
            5 -> { mainBinding.imgNumber.setImageResource(R.drawable.landline)
                    Rechargetype= "Prepaidreport"}
            9 -> { mainBinding.imgNumber.setImageResource(R.drawable.landline)
                Rechargetype= "Leadline"}

            else -> Toast.makeText(this@TopServicesActivity,"Please go to back.... \n try again.",Toast.LENGTH_LONG).show()
        }
    }

    fun monClick()
    {
        mainBinding.toolbarLayout.back.setOnClickListener {
             super.onBackPressed()
             }
        mainBinding.txtOperator.setOnClickListener { gerrateOperatorList(operator_type) }
        mainBinding.txtViewAll.setOnClickListener { viewAll() }
        mainBinding.txtBrowesPlane.setOnClickListener {
            if(!mainBinding.txtOperator.text.toString().isEmpty()) {
                pref.writeStringPreference(Constances.PREF_temp_number,mainBinding.edtNumber.text.toString())
                val intent1 = Intent(this@TopServicesActivity,SelectRecharge_Palne_Activity::class.java)
                startActivity(intent1)
            } else {
                Toast.makeText(this,"Please First Select Operator.",Toast.LENGTH_LONG).show()
            }
        }
        mainBinding.btnProcess.setOnClickListener {
            if(mainBinding.edtNumber.text.toString().length<8&&mainBinding.txtOperator.text.toString().isEmpty()&&mainBinding.txtSelectedRechargePlane.text.toString().isEmpty()) {
                mainBinding.edtNumber.setError("Please Enter Valid Number")
                mainBinding.txtOperator.setError("Please select Operator")
                mainBinding.txtSelectedRechargePlane.setError("Please Enter Amount")
            }  else if(mainBinding.edtNumber.text.toString().length<8) {
                mainBinding.edtNumber.setError("Please Enter Valid Number")
            } else if(mainBinding.txtOperator.text.toString().isEmpty()) {
                mainBinding.txtOperator.setError("Please select Operator")
            } else if(mainBinding.txtSelectedRechargePlane.text.toString().isEmpty()) {
                mainBinding.txtSelectedRechargePlane.setError("Please Enter Amount")
            } else {

                mainBinding.edtNumber.setError(null)
                mainBinding.txtOperator.setError(null)
                mainBinding.txtSelectedRechargePlane.setError(null)
                rechrgeProcess()
            }
        }
    }

    fun rechrgeProcess() {
        pausingDialog?.show()

        var apiInterface: ApiInterface = RetrofitManager(this@TopServicesActivity).instance!!.create(ApiInterface::class.java)

        Log.e(">>",pref.ReadStringPreferences(Constances.PREF_Mobile)+" _ "+
                pref.ReadStringPreferences(Constances.PREF_Login_password)+" _ "+
                mainBinding.txtSelectedRechargePlane.text.toString()+" _ "+
                pref.ReadStringPreferences(Constances.PREF_operator_code)+" _ "+mainBinding.edtNumber.text.toString())
        apiInterface.getRechargeprcess(pref.ReadStringPreferences(Constances.PREF_Mobile),
            pref.ReadStringPreferences(Constances.PREF_Login_password),
            mainBinding.txtSelectedRechargePlane.text.toString(),
            pref.ReadStringPreferences(Constances.PREF_operator_code),mainBinding.edtNumber.text.toString())
            .enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@TopServicesActivity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
                    pausingDialog?.dismiss()
                    showDialog("Something wrong","3")
                }

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    pausingDialog?.dismiss()
                    if(response.isSuccessful) {
                        Log.e(TAG, "onResponse: "+response.body().toString())
                        var mJSONObject= JSONObject(response.body().toString())
                        Toast.makeText(this@TopServicesActivity,mJSONObject.getString("Message").toString()+" ",Toast.LENGTH_LONG).show()
                        if (mJSONObject.getString("Error").toLowerCase().equals("false") && mJSONObject != null
                        ) {
                            if(mJSONObject.has("Data") && !mJSONObject.isNull("Data") ) {
                            var mjsonArray:JSONArray=mJSONObject.getJSONArray("Data")
                            var mjsondata=mjsonArray.getJSONObject(0)
                            if(mjsondata.getString("Status").equals("Success")) {
                                showDialog(mjsondata.getString("Status"),"2")
                            } else {
                                showDialog(mjsondata.getString("Status"),"1")
                            }
                        } else { showDialog(mJSONObject.getString("Message").toString(),"3") }
                    } else { showDialog(mJSONObject.getString("Message").toString(),"3") }
                    } else {
                        showDialog("Something wrong","3") }
                }
            })
    }

    fun getrechargereport()
    {

        mainBinding.imgEmptylist.visibility = View.GONE
        mainBinding.mainProgress.visibility = View.VISIBLE
        var apiInterface: ApiInterface = RetrofitManager(this@TopServicesActivity).instance!!.create(ApiInterface::class.java)

        apiInterface.getrechargereport(pref.ReadStringPreferences(Constances.PREF_Msrno),last_history_ID.toString(),Rechargetype).
        enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@TopServicesActivity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
                mainBinding.imgEmptylist.visibility = View.VISIBLE
                mainBinding.mainProgress.visibility = View.GONE
//                adapter?.removeLoadingFooter()
                Log.e("//","5")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(mainBinding.mainSwiperefresh.isRefreshing == true) {
                    mainBinding.mainSwiperefresh.setRefreshing(false) } else { }

                mainBinding.imgEmptylist.visibility = View.GONE
                mainBinding.mainProgress.visibility = View.GONE

                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.toString())
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
//                    Toast.makeText(this@TopServicesActivity,mJsonObject.getString("Message")+" ",Toast.LENGTH_LONG).show()

                    if(mJsonObject.has("Data") && !mJsonObject.isNull("Data") ) {

                        var mJsonArray = mJsonObject.getJSONArray("Data")

                        if (mJsonObject.getString("Error").toLowerCase()
                                .equals("false") && mJsonArray != null
                        ) {

                            for (i in 0 until mJsonArray.length()) {

                                var JsonObjectData = mJsonArray.getJSONObject(i)
                                var mRechargereportModel: RechargePlaneModel =
                                    RechargePlaneModel()
                                mRechargereportModel.historyID =
                                    JsonObjectData.getString("HistoryID").toInt()
                                mRechargereportModel.mobileno = JsonObjectData.getString("Mobileno")
                                mRechargereportModel.rechargeAmount =
                                    JsonObjectData.getString("RechargeAmount").toDouble()
                                mRechargereportModel.transID = JsonObjectData.getString("TransID")
                                mRechargereportModel.status = JsonObjectData.getString("Status")
                                mRechargereportModel.addDate = JsonObjectData.getString("AddDate")
                                mRechargereportModel.operatorName =
                                    JsonObjectData.getString("OperatorName")
                                mRechargereportModel.operaotimageurl =
                                    JsonObjectData.getString("operaotimageurl")
                                mRechargereportModel.rechargetime =
                                    JsonObjectData.getString("rechargetime")
                                rechargeReportHistory.add(mRechargereportModel)
                                Log.d("!!" + TAG, "data " + i.toString() + " " + mRechargereportModel.operatorName)
                                last_history_ID = JsonObjectData.getString("HistoryID").toInt()
                            }
                            adapter?.notifyDataSetChanged()
                            adapter?.addLoadingFooter()
                            isLoading = false

                            Log.e("//", rechargeReportHistory.size.toString())
                            if (rechargeReportHistory.size < 9) {
                                adapter?.removeLoadingFooter()
                            }





                        } else {
                            mainBinding.imgEmptylist.visibility = View.VISIBLE
                            mainBinding.mainProgress.visibility = View.GONE


                            Log.e("//","1")
                        }
                    } else {
                        mainBinding.imgEmptylist.visibility = View.VISIBLE
                        mainBinding.mainProgress.visibility = View.GONE

                        Log.e("//","2")
                    }
                } else {
                    mainBinding.imgEmptylist.visibility = View.VISIBLE
                    mainBinding.mainProgress.visibility = View.GONE

                    Log.e("//","3")
                }
            }
        })
    }

    fun gerrateOperatorList(operator_type:String)
    {
        pausingDialog?.show()
        var operatorList:ArrayList<OperatorModel> = java.util.ArrayList()
        var apiInterface: ApiInterface = RetrofitManager(this@TopServicesActivity).instance!!.create(ApiInterface::class.java)

        apiInterface.getoperatordetails(operator_type).enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@TopServicesActivity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
                pausingDialog?.dismiss()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())
                    var operatorDialog : CustomDialog
                    Toast.makeText(this@TopServicesActivity,mJsonObject.getString("Message")+" ",Toast.LENGTH_LONG).show()
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
                                this@TopServicesActivity,
                                operatorList,
                                mainBinding.txtOperator,mOperator_CODE
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
//
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
    private fun setupViewPager(viewPager: ViewPager,mFragmentManager: FragmentManager,mActionBar: ActionBar) {

        var pref = DynamicTabFragmentPagerAdapter(this@TopServicesActivity,mFragmentManager,viewPager,mActionBar)
    }
    fun cln() {
        Log.d(TAG, "setview1 "+pref.ReadStringPreferences(Constances.PREF_amt))
        Log.d(TAG, "setview2 "+pref.ReadStringPreferences(Constances.PREF_operator_code))
        Log.d(TAG, "setview3 "+pref.ReadIntPreferences("opt"))
        pref.writeStringPreference(Constances.PREF_amt,"")
        pref.writeStringPreference(Constances.PREF_operator_code,"")
        pref.writeStringPreference(Constances.PREF_temp_number,"")
        pref.writeIntPreference("opt",0)
    }

    fun showDialog(title:String,type:String){
        Log.d("@@"+TAG, "showDialog: ")
        SweetAlertDialog(this, type.toInt())
            .setTitleText(title)
//            .setContentText(title)
            .setConfirmText("OK")
            .setConfirmClickListener { sDialog ->
                sDialog.dismiss()
            }
            .show()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        cln()
        startActivity(Intent(this@TopServicesActivity,DashboardActivity::class.java))
    }
}
public class Once {
    private val done: AtomicBoolean = AtomicBoolean()
    fun run(task: Runnable) {
        if (done.get()) return
        if (done.compareAndSet(false, true)) {
            task.run()
        }
    }

}