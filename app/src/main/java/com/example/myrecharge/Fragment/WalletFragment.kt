package com.example.myrecharge.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.myrecharge.Helper.Local_data
import com.example.myrecharge.Helper.Constances
import com.example.myrecharge.R
import com.example.myrecharge.databinding.FragmentWalletBinding

class WalletFragment : Fragment() {
    lateinit var thiscontext: Context
    lateinit var mainBinding : FragmentWalletBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        mainBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_wallet, container, false)
        thiscontext=container!!.context
        initView()

        mainBinding.btnBtnIncomme.setOnClickListener {

        }

        mainBinding.btnRpwallet.setOnClickListener {
            setFram1(PassbookFragment())
        }

        return mainBinding.root
    }
    fun initView()
    {
        var pref= Local_data(thiscontext)
        pref.setMyappContext(thiscontext)

        mainBinding.rpBalance.setText(pref.ReadStringPreferences(Constances.PREF_Balance))
//        mainBinding.tIncome.setText(pref.ReadStringPreferences(Constances.PREF_balance2))
    }

    fun setFram1(fram: Fragment)
    {
        val newFragment = fram
        requireFragmentManager().beginTransaction()
            .replace(R.id.wallet_fram, newFragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}