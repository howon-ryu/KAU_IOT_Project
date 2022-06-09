package com.example.esp32_android;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView ID;
        protected TextView DATE;
        protected TextView HUMID;
        protected TextView DUST;
        protected TextView TEMP;

        public CustomViewHolder(View view) {
            super(view);
            this.ID = (TextView) view.findViewById(R.id.textView_list_ID);
            this.DATE = (TextView) view.findViewById(R.id.textView_list_DATE);
            this.HUMID = (TextView) view.findViewById(R.id.textView_list_HUMID);
            this.TEMP = (TextView) view.findViewById(R.id.textView_list_TEMP);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.ID.setText(mList.get(position).getMember_ID());
        viewholder.DATE.setText(mList.get(position).getMember_DATE());
        viewholder.TEMP.setText(mList.get(position).getMember_TEMP());
        viewholder.HUMID.setText(mList.get(position).getMember_HUMID());


    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}
