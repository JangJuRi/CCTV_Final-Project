package com.example.cctv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<BoardList> data;
    private int layout;

    public BoardAdapter(Context context, int layout,ArrayList<BoardList> data) {
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
        BoardList boardList = data.get(position);

        TextView rank = (TextView)convertView.findViewById(R.id.BoardRank_textview);
        rank.setText(""+boardList.getRank());

        TextView title = (TextView)convertView.findViewById(R.id.BoardTitle_textview);
        title.setText(boardList.getTitle());

        return convertView;
    }
}
