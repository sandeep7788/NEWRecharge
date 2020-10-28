package com.example.myrecharge.Custom_;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
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

public class CustomDailog1 extends DialogFragment {


    private View view;
    public ImageView dialog_cancel_btn;
    int textlength=0;
    public Context context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    TabLayout tabLayout;
    ViewPager viewPager;
    String tabname, tabid;
    private TabAdapter adapter;
    FragmentManager fm;

    public CustomDailog1(Context context)
    {
        this.context=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_dialog_operatorlist, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.TOP);

        getBlogTab();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        int screenHeight = (int) (metrics.heightPixels * 0.95);

        getDialog().getWindow().setLayout(screenWidth, screenHeight);

       ViewPager addonsviewpager = (ViewPager) view.findViewById(R.id.pager);
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
                }
            }
            @Override
            public void onFailure(Call<ModelRequestdetails> call, Throwable t) {

            }
        });
    }
}
