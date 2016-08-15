package com.zhou12314.myrecyleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoug on 2016/8/11.
 */
public class MyRecyleViewAdapter extends RecyclerView.Adapter<MyRecyleViewAdapter.MyHolder>{
    private  List<String> list = new ArrayList<>();
    private  Context  context;

    public  MyRecyleViewAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
    }



    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.myrecyleview_item,parent,false));
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.tv_item.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class  MyHolder extends RecyclerView.ViewHolder{
        TextView tv_item;
        public MyHolder(View itemView) {
            super(itemView);
             tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
