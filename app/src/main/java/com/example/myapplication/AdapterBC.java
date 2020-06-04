package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterBC extends BaseAdapter {

    Context context;
    int layout;
    List<Integer> icons;
    List<String> texts;

    public AdapterBC(Context c, int l, List<Integer> hinh, List<String> chu) {
        context = c;
        layout = l;
        icons = hinh;
        texts = chu;
    }


    @Override
    public int getCount() {
        return texts.size();
    }

    @Override
    public Object getItem(int position) {
        return texts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE.toString());
        view = inf.inflate(layout, null);
        ImageView hinhanh = view.findViewById(R.id.cellbc_hinh);
        TextView textView = view.findViewById(R.id.cellbc_text);
        textView.setText(texts.get(position));
        hinhanh.setImageResource(icons.get(position));
        return view;
    }
}
