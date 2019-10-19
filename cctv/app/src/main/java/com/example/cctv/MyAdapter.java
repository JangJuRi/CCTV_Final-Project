package com.example.cctv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<News> sample;

    public MyAdapter(Context context, ArrayList<News> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public News getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.news_listview_layout, null);

        TextView NewsTitle = (TextView)view.findViewById(R.id.NewsTitle);
        TextView NewsDs = (TextView)view.findViewById(R.id.NewsDs);

        NewsTitle.setText(sample.get(position).getTitle());
        NewsDs.setText(sample.get(position).getDescription());

        return view;
    }
}