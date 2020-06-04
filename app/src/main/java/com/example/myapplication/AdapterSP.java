package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterSP extends BaseAdapter {

    Context context;
    int layout;
    List<Item> list;

    public AdapterSP(Context c, int l, List<Item> li) {
        context = c;
        layout = l;
        list = li;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE.toString());
        view = inf.inflate(layout, null);
        ImageView image = view.findViewById(R.id.cellxhd_hinhsanpham);
        TextView ten = view.findViewById(R.id.cellxhd_tensanpham);
        TextView gia = view.findViewById(R.id.cellxhd_giasanpham);
        image.setImageBitmap(list.get(position).getImage());
        ten.setText(list.get(position).getName());
        String str = String.format("%,d", list.get(position).getPrice());
        gia.setText(str);
        return view;
    }
}
