package com.example.cctv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<CommentList> data;
    private int layout;

    public CommentAdapter(Context context, int layout, ArrayList<CommentList> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(layout,parent,false);
        }
        CommentList commentList = data.get(position);

        TextView Cwriter = (TextView)convertView.findViewById(R.id.CommentWriterTextview);
        Cwriter.setText(""+commentList.getCWriter());

        TextView Cdescription = (TextView)convertView.findViewById(R.id.CommentDsTextview);
        Cdescription.setText(commentList.getCDescription());

        return convertView;
    }
}
