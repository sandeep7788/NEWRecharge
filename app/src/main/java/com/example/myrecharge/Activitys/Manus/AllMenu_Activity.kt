package com.example.myrecharge.Activitys.Manus

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myrecharge.Custom.CustomDialog
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.Models.OperatorModel
import com.example.myrecharge.R
import com.example.myrecharge.databinding.ActivityPayBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class AllMenu_Activity : AppCompatActivity(), DialogInterface.OnClickListener, OnItemClickListener
{
    lateinit var mainBinding : ActivityPayBinding
    var pref= Local_data(this@AllMenu_Activity)
    var TAG="@@pay_activty"
    lateinit var title:ArrayList<String>
    lateinit var operator_image:ArrayList<String>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        mainBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_pay
            )
        pref.setMyappContext(this@AllMenu_Activity)
        title= ArrayList()
        operator_image= ArrayList()
    mainBinding.toolbar.back.setOnClickListener { super.onBackPressed() }
    mainBinding.btnScan.setOnClickListener { startActivity(Intent(this@AllMenu_Activity,Scanner_Activity::class.java)) }

        mainBinding.btnPay.setOnClickListener {
            gerrateOperatorList()
//            operatorDialog.onClick(this@Pay_Activity)
//            dialog(this@Pay_Activity,operatorList)
            if(mainBinding.edtMemberId.text.toString().isEmpty())
            {
                mainBinding.edtMemberId.setError("Enter value")
            }
            else
            {

            }
        }
    }

    override fun onResume() {
        super.onResume()
//        codeScanner.startPreview()
    }

    override fun onPause() {
//        codeScanner.releaseResources()
        super.onPause()
    }

    fun gerrateOperatorList()
    {
        var operatorList:ArrayList<OperatorModel> = java.util.ArrayList()
        var apiInterface: ApiInterface = RetrofitManager(this@AllMenu_Activity).instance!!.create(ApiInterface::class.java)

        apiInterface.getoperatordetails("1").enqueue(object : Callback<JsonObject>
        {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@AllMenu_Activity,t.message.toString()+" ",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful) {
                    Log.d(TAG, "onResponse: "+response.body().toString())
                    var mJsonObject = JSONObject(response.body().toString())

                    var operatorDialog : CustomDialog

                    Toast.makeText(this@AllMenu_Activity,mJsonObject.getString("Message")+" ",Toast.LENGTH_LONG).show()
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
                                this@AllMenu_Activity,
                                operatorList,
                                mainBinding.textView
                            )
//                        operatorDialog.getWindow()!!.setBackgroundDrawable(ColoWi);
//                        operatorDialog.window!!.setBackgroundDrawable(R.color.blue_btn_bg_pressed_color)
//                        operatorDialog.getWindow()!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        //        requestWindowFeature(Window.FEATURE_NO_TITLE);

                        operatorDialog!!.getWindow()!!.setLayout(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.FILL_PARENT
                        )
                        operatorDialog.show()
                    }
                }
            }
        })
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        Log.d(TAG, "onClick: "+p1.toString())
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        Log.d(TAG, "onClick: "+p2.toString())
    }
    
/*
    fun dialog(
        context: Context?,
        array_data: java.util.ArrayList<OperatorModel>
    ) {
        val dialog_data = Dialog(context!!)
        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_data.window!!.setGravity(Gravity.CENTER)
        dialog_data.setContentView(R.layout.custom_dialog_layout)
        val lp_number_picker = WindowManager.LayoutParams()
        val window = dialog_data.window
        lp_number_picker.copyFrom(window!!.attributes)
        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.CENTER)
        window.attributes = lp_number_picker
        val alertdialog_textview =
            dialog_data.findViewById<View>(R.id.alertdialog_textview) as TextView
        //        alertdialog_textview.setText(selected_string);
        alertdialog_textview.hint = "selected_string"
        Log.e(">>", alertdialog_textview.text.toString() + " ")
        val dialog_cancel_btn =
            dialog_data.findViewById<View>(R.id.dialog_cancel_btn) as Button
        dialog_cancel_btn.setOnClickListener {
            if (dialog_data != null) {
                dialog_data.dismiss()
                dialog_data.cancel()
            }
        }
        val filterText =
            dialog_data.findViewById<View>(R.id.alertdialog_edittext) as EditText
        val alertdialog_Listview =
            dialog_data.findViewById<View>(R.id.alertdialog_Listview) as ListView
        alertdialog_Listview.choiceMode = ListView.CHOICE_MODE_SINGLE

//            val adapter = ComponentAdapter(
//                context,R.layout.single_item,array_data!!)


        title.add("title")
        title.add("title")
        title.add("title")
        title.add("title")
        operator_image.add("image")
        operator_image.add("image")
        operator_image.add("image")
        operator_image.add("image")
        val list =
            java.util.ArrayList<HashMap<String, String>>()

        var adapter1 = SimpleAdapter(
            this,
            list,
            R.layout.single_item,
            arrayOf("img", "name", "dob", "anniversary"),
            intArrayOf(R.id.icon, R.id.title)
        )
        val adapter =
            CustomAlertAdapter(context as Activity?, operatorList)
        alertdialog_Listview.adapter = adapter
        alertdialog_Listview.onItemClickListener =
            OnItemClickListener { a, v, position, id -> //
                //       Utility.hideKeyboard(context,v);

                //data.setText(String.valueOf(a.getItemAtPosition(position)));
                if (dialog_data != null) {
                    dialog_data.dismiss()
                    dialog_data.cancel()
                }
            }
        filterText.addTextChangedListener(object : TextWatcher {
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
            }

            override fun afterTextChanged(s: Editable) {
//                    adapter.filter.filter(s)
//                    adapter.filter.filter(s)
            }
        })
        dialog_data.show()
    }
*/
}