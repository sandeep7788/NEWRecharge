package com.example.myrecharge.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.Adapter.Adapter
import com.example.myrecharge.Helper.*
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentPostpaidBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class Postpaid_fragment : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentPostpaidBinding
    lateinit var arrayAdapter:ArrayAdapter<String>
    var op_code="0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { mainBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_postpaid, container, false
    )

        thiscontext=container!!.context
        arrayAdapter=ArrayAdapter<String>(thiscontext, android.R.layout.select_dialog_singlechoice)
        setFram(Adapter())
        mainBinding.operator.setOnClickListener {
//            operator_setup()
        }
        setFram(Adapter())

        mainBinding.ivPhoneBook.setOnClickListener {
            browseContacts()
        }

        mainBinding.btnRecharge.setOnClickListener {
            if(mainBinding.edtMobile.toString().isEmpty())
            {
                mainBinding.edtMobile.setError("enter value")
            }
            else  if(mainBinding.edtAmount.toString().isEmpty())
            {
                mainBinding.edtAmount.setError("enter value")
            }
            else{
//                recharge()
            }

        }


        return mainBinding.root
    }
    private fun browseContacts() {
        val pickContactIntent =
            Intent("android.intent.action.PICK", Uri.parse("content://contacts"))
        pickContactIntent.type = "vnd.android.cursor.dir/phone_v2"
        startActivityForResult(pickContactIntent, 1)
    }

    fun setFram(fram: Fragment)
    {


        val newFragment = fram
        requireFragmentManager().beginTransaction()
            .replace(R.id.f1, newFragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()

    }

    fun dialog(context: Context)
    {

        var strName="0"
        val builderSingle: AlertDialog.Builder = AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
        builderSingle.setIcon(R.drawable.prepaid)
        builderSingle.setTitle("Select Any Operator :-")

        builderSingle.setNegativeButton("cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

        builderSingle.setAdapter(arrayAdapter,
            DialogInterface.OnClickListener { dialog, which ->
                strName = arrayAdapter.getItem(which).toString()
//                op_code=data.get(which).operatorCode.toString()

                val builderInner: AlertDialog.Builder = AlertDialog.Builder(context)
                builderInner.setMessage(strName)
                builderInner.setTitle("Select Any Operator :- ")
                builderInner.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                builderInner.show()
                mainBinding.operator.setText(strName+" ")

            })
        builderSingle.show()
    }



}