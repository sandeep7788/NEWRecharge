package com.example.myrecharge.Fragment

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.myrecharge.Custom_.QrDialog
import com.example.myrecharge.Helper.ApiInterface
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.RetrofitManager
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentSettingBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SettingFragment : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentSettingBinding
    var pref= Local_data(context)
    var TAG=">>SettingFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setting, container, false
        )
        thiscontext=container!!.context
        pref.setMyappContext(context)
        mainBinding.toolbarLayout.back.visibility=View.GONE


        OnClick()
        return mainBinding.root
    }
    fun OnClick() {
        mainBinding.btnChangePassword.setOnClickListener {
            showDialog()
        }
        mainBinding.btnMtEarning.setOnClickListener { showDialog_commingSoon() }
        mainBinding.btnMyRaferal.setOnClickListener { showDialog_commingSoon() }
        mainBinding.vtnBankDetails.setOnClickListener { setFragment() }
        mainBinding.btnComplain.setOnClickListener { complainDialog() }
        mainBinding.btnLogout.setOnClickListener {
            AlertDialog.Builder(thiscontext)
                .setMessage("Are you sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        pref.logout(true)
                    }

                })
                .setNegativeButton("No", null)
                .show()

        }
        mainBinding.btnQrCode.setOnClickListener {

            var mQrDialog : QrDialog
            mQrDialog=
                QrDialog(activity)
            mQrDialog.getWindow()!!.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
            mQrDialog.show()

        }
    }
    private fun setFragment() {
        val fragmentManager: FragmentManager? = fragmentManager
        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        transaction.add(R.id.fram_setting,BankDetailsFragment(),null) //replace

        transaction.addToBackStack(null) //need to add in backstack or not. if not set as null

//        fragmentManager?.addOnBackStackChangedListener(thiscontext)
        transaction.commit()
    }
    protected fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        val view: View = activity?.layoutInflater!!.inflate(R.layout.dialog_change_password_, null)

        dialog.setContentView(view)
        val edt_old = view.findViewById<View>(R.id.edt_old) as EditText
        val edt_newpassword = view.findViewById<View>(R.id.edt_newpassword) as EditText
        val edt_re_enter_new_password = view.findViewById<View>(R.id.edt_re_enter_new_password) as EditText
        val btn_submit = view.findViewById<View>(R.id.btn_submit) as Button
        val img_close = view.findViewById<View>(R.id.img_close) as ImageView
        img_close.setOnClickListener {
            dialog.dismiss()
        }

        btn_submit.setOnClickListener {
            if(edt_old.text!!.isEmpty())
            {
                edt_old.setError("Please Enter Login-ID")
            }
            else if(edt_newpassword.text!!.isEmpty())
            {
                edt_newpassword.setError("Please Enter Password")
            }
            else if(edt_re_enter_new_password.text!!.isEmpty())
            {
                edt_re_enter_new_password.setError("Please Enter Password")
            }
            else if(!(edt_newpassword.text?.toString()?.trim() == edt_re_enter_new_password.text?.toString()?.trim()))
            {
                Log.e(
                    ">>",
                    edt_newpassword.text?.toString()
                        ?.trim() + "_" + edt_re_enter_new_password.text?.toString()?.trim()
                )
                Toast.makeText(thiscontext, "new and conform password not same!", Toast.LENGTH_LONG).show()

            }
            else
            {
                changePasssword(
                    edt_old.text.toString().trim(),
                    edt_newpassword.text.toString().trim()
                )

            }
        }
        dialog.show()
        val window: Window? = dialog.getWindow()
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
    }
    protected fun complainDialog() {
        val dialogBuilder: android.app.AlertDialog? = android.app.AlertDialog.Builder(thiscontext).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_complain, null)

        val editText = dialogView.findViewById(R.id.edt_comment) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button

        button2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialogBuilder?.dismiss()
            }


        })
        button1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialogBuilder?.dismiss()
            }


        })

        dialogBuilder?.setView(dialogView)
        dialogBuilder?.show()
    }

    fun changePasssword(oldpass: String, newpass: String) {

        var apiInterface: ApiInterface = RetrofitManager(thiscontext).instance!!.create(
            ApiInterface::class.java
        )

        apiInterface.ChangePassword(
            pref.ReadStringPreferences(Constances.PREF_Msrno),
            oldpass,
            newpass, " "
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d(TAG, "onResponse: " + response.toString())
                if (response.isSuccessful) {
                    var mjsonObject: JSONObject = JSONObject(response.body().toString())

                    Log.d(TAG, "onResponse: url" + mjsonObject.getString("Error"))

                    if (mjsonObject.getString("Error").toLowerCase().equals("false")) {
                        Toast.makeText(
                            activity,
                            " " + mjsonObject.getString("Message"),
                            Toast.LENGTH_LONG
                        ).show()
                        showDialog(mjsonObject.getString("Message") + " ", "1")

                    } else {
                        showDialog(mjsonObject.getString("Message") + " ", "3")
                    }
                } else {
                    showDialog("Something wrong", "3")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(activity, " " + t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun showDialog(title: String, type: String){
        Log.d("@@" + TAG, "showDialog: ")
        SweetAlertDialog(activity, type.toInt())
            .setTitleText(title)
            .setContentText(title)
            .setConfirmText("OK")
            .setConfirmClickListener { sDialog ->
                sDialog.dismiss()
            }
            .show()

    }
    fun showDialog_commingSoon(){
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Coming soon")
            .setContentText("work on progress")
            .setConfirmText("OK")
            .setConfirmClickListener { sDialog ->
                sDialog.dismiss()
            }
            .show()
    }
}