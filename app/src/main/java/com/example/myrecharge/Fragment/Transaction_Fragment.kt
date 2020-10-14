package com.example.myrecharge.Fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class Transaction_Fragment: Fragment() {
    lateinit var mcontext: Context
    lateinit var mainBinding : FragmentTransactionBinding
    var cal = Calendar.getInstance()
    var date_type=0
    var pref= Local_data(context)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { mainBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_transaction, container, false
    )
        mcontext=container!!.context
        pref.setMyappContext(mcontext)
        settype()
//        get_operator_list()

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        mainBinding.fromDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    mcontext,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
                date_type = 1
            }
        })

        mainBinding.toDate.setOnClickListener {
            mainBinding.fromDate.performClick()
            date_type=2
        }

        return mainBinding.root
    }

    private fun updateDateInView() {

        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        if(date_type==1)
        {
            mainBinding.fromDate!!.text = sdf.format(cal.getTime())
        }
        else if(date_type==2)
        {
            mainBinding.toDate!!.text = sdf.format(cal.getTime())
        }
        else if(date_type==0)
        {

        }
    }

    fun settype()
    {
        val languages = resources.getStringArray(R.array.type)
        val spinner = mainBinding.spinnerType

        if (spinner != null) {
            val adapter = ArrayAdapter(
                mcontext,
                android.R.layout.simple_spinner_item, languages
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        mcontext, " " +
                                "" + languages[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
}