package com.example.myrecharge.Activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.myrecharge.Custom.CustomAlertAdapter;
import com.example.myrecharge.Models.OperatorModel;
import com.example.myrecharge.R;
import java.util.ArrayList;

public class ValueFilter implements  OnItemClickListener {

    public Button btn_listviewdialog=null;
    //public String TitleName[]={"Sunil Gupta","Ram Chnadra"," Abhishek Tripathi","Amit Verma","Sandeep Pal","Awadhesh Diwakar","Shishir Verma","Ravi Vimal","Prabhakr Singh","Manish Srivastva","Jitendra Singh","Surendra Pal"};
    public ArrayList<OperatorModel> TitleName = new ArrayList<>();
    public ArrayList<OperatorModel> array_sort=new ArrayList<>();
    int textlength=0;
    public AlertDialog myalertDialog=null;
    public Context context;
    TextView textView;

    public ValueFilter(Context context, final ArrayList<OperatorModel> TitleName, TextView textview){
        this.context=context;
        this.TitleName=TitleName;
        this.textView=textview;
        onClick();
    }

    public void onClick() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context,R.layout.custom_dialog_layout);


        Log.d("##TitleName", "onResponse: "+TitleName.get(2).getOperatorName());



        final EditText editText = new EditText(context);
        final ListView listview=new ListView(context);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.prepaid, 0, 0, 0);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(listview);
        myDialog.setView(layout);
        CustomAlertAdapter arrayAdapter=new CustomAlertAdapter((Activity) context, TitleName);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(this);
        editText.addTextChangedListener(new TextWatcher()

        {

            public void afterTextChanged(Editable s){



            }

            public void beforeTextChanged(CharSequence s,

                                          int start, int count, int after){



            }

            public void onTextChanged(CharSequence s, int start, int before, int count)

            {

                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                textlength = editText.getText().length();

                array_sort.clear();

                for (int i = 0; i < TitleName.size(); i++)

                {

                    if (textlength <= TitleName.get(i).getOperatorName().length())
                    {
                        if(TitleName.get(i).getOperatorName().toLowerCase().contains(editText.getText().toString().toLowerCase().trim()))

                        {

                            array_sort.add(TitleName.get(i));

                        }

                    }

                }

                listview.setAdapter(new CustomAlertAdapter((Activity) context,array_sort));

            }

        });

        myDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {


            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });

        myalertDialog=myDialog.show();



    }

    @Override
    public void onItemClick(AdapterView arg0, View arg1, int position, long arg3) {



        myalertDialog.dismiss();

//        String strName=TitleName.get(position).getOperatorName().toString();

//        txt_item.setText(strName);
        Log.e(">>itemclick", String.valueOf(position));
        textView.setText(" "+TitleName.get(position).getOperatorName().toString());

    }
}