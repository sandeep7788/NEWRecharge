package com.example.myrecharge.Activitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myrecharge.Models.OperatorModel;
import com.example.myrecharge.R;

public class ComponentAdapter extends ArrayAdapter<OperatorModel> {

    public ComponentAdapter(Context context, int textViewResourceId, java.util.ArrayList<OperatorModel> objects)
    {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View curView = convertView;
        if (curView == null)
        {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.single_item, null);
        }

        OperatorModel cp = getItem(position);
        TextView title = (TextView) curView.findViewById (R.id.title);


        title.setText(cp.getOperatorName());

        return curView;
    }
}