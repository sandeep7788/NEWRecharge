package com.example.myrecharge.Custom_;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myrecharge.Fragment.RechargePlaneFragment;
import com.example.myrecharge.Helper.ApiInterface;
import com.example.myrecharge.Helper.RetrofitManager;
import com.example.myrecharge.Models.ModelRequestdetails;
import com.example.myrecharge.R;
import com.example.myrecharge.TabAdapter;
import com.google.android.material.tabs.TabLayout;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDialog_OperatorList extends Dialog {


    public Context context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    TabLayout tabLayout;
    ViewPager viewPager;
    String tabname, tabid;
    private TabAdapter adapter;
    FragmentManager fm;

    public CustomDialog_OperatorList(Context context,FragmentManager fm) {
        super(context,R.style.FullWidth_Dialog);
        this.context=context;
        this.fm=fm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(WindowManager.LayoutParams.FILL_PARENT);
        setContentView(R.layout.custom_dialog_operatorlist);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setAttributes(layoutParams);
        viewPager =findViewById(R.id.pager1);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tabLayout = findViewById(R.id.tabs);

//        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();


        getBlogTab();
    }

    private void getBlogTab() {
        ApiInterface apiInterface = new RetrofitManager(context).getInstance().create(ApiInterface.class);
        Call<ModelRequestdetails> call = apiInterface.GetPlandetails("AT");
        call.enqueue(new Callback<ModelRequestdetails>() {
            @Override
            public void onResponse(Call<ModelRequestdetails> call, Response<ModelRequestdetails> response) {

                if (response.isSuccessful()) {
                    ModelRequestdetails childResult = response.body();

                    for (int j = 0; j < childResult.getData().size(); j++) {
                        tabname = childResult.getData().get(j).getCategory();
                        tabid = childResult.getData().get(j).getCategory();
                        mFragmentTitleList.add(tabname);
                        adapter.addFragment(new RechargePlaneFragment(j), tabname);
                    }
                    viewPager.setAdapter(adapter);
                    viewPager.setOffscreenPageLimit(childResult.getData().size() + 1);
                    tabLayout.setupWithViewPager(viewPager);
                    for (int i = 0; i < mFragmentList.size(); i++) {
                        mFragmentTitleList.add("no title");
                    }
                    adapter = new TabAdapter(fm);
                }
            }
            @Override
            public void onFailure(Call<ModelRequestdetails> call, Throwable t) {

            }
        });
    }
}