package com.example.myrecharge.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myrecharge.Models.RechargePlaneModel;
import com.example.myrecharge.R;
import java.util.ArrayList;
import java.util.List;

public class TopServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<RechargePlaneModel> movies;
    private Context context;

    private boolean isLoadingAdded = false;
    String TAG="@@PagingAdapter";

    public TopServicesAdapter(Context context, List<RechargePlaneModel> movies) {
        this.context = context;
        this.movies=movies;
        movies = new ArrayList<>();
    }

    public List<RechargePlaneModel> getMovies() {
        return movies;
    }

    public void setMovies(List<RechargePlaneModel> movies) {
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.d(TAG, "onCreateViewHolder: "+viewType);
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    public RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list1, parent, false);
        viewHolder = new ItemList(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RechargePlaneModel movie = movies.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ItemList mItemList = (ItemList) holder;

                try{
                if(movie.getTransID()==null){
//                    mItemList.txt_id.setText("_");
                }else{
                    mItemList.txt_id.setText(movie.getMobileno().toString()+" ");
                    mItemList.txt_date.setText("on "+movie.getAddDate().toString()+" "+movie.getRechargetime().toString());
                    mItemList.txt_recharge.setText("₹ "+movie.getRechargeAmount().toString());
                    mItemList.txt_status.setText(" "+movie.getStatus());

                    Glide.with(context)
                            .load(movie.getOperaotimageurl())
                            .thumbnail(0.5f)
                            .into(mItemList.img_operator);
                    if(movie.getStatus().toString().toLowerCase().equals("success")) {
                        mItemList.txt_status.setTextColor(Color.parseColor("#0b892e"));
                    } else {
                        mItemList.txt_status.setTextColor(Color.parseColor("#ed1b24"));
                    }

                }

                }catch (Exception e){
                    Toast.makeText(context, " "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void add(RechargePlaneModel mc) {
        movies.add(mc);
        notifyItemInserted(movies.size() - 1);
    }

    public void addAll(List<RechargePlaneModel> mcList) {
        for (RechargePlaneModel mc : mcList) {
            add(mc);
        }
    }

    public void remove(RechargePlaneModel city) {
        int position = movies.indexOf(city);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new RechargePlaneModel());
    }
    public void addLoadingFooter1() {
        isLoadingAdded = false;
        add(new RechargePlaneModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movies.size() - 1;
        RechargePlaneModel item = getItem(position);

        if (item != null) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RechargePlaneModel getItem(int position) {
        return movies.get(position);
    }

    protected class ItemList extends RecyclerView.ViewHolder {
        private TextView txt_id;
        private ImageView img_operator;
        private TextView txt_recharge;
        private TextView txt_status;
        private TextView txt_date;

        public ItemList(View itemView) {
            super(itemView);

            txt_id = (TextView) itemView.findViewById(R.id.txt_id);
            img_operator = (ImageView) itemView.findViewById(R.id.img_operator);
            txt_recharge = (TextView) itemView.findViewById(R.id.txt_recharge);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
