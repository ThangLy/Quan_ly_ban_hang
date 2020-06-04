package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterLS extends BaseAdapter {

    Context context;
    int layout;
    List<History> list;

    public AdapterLS(Context c, int l, List<History> li) {
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
        TextView gia = view.findViewById(R.id.cellls_giatien);
        TextView thoigian = view.findViewById(R.id.cellls_thoigian);
        String str = String.format("%,d", list.get(position).getTotal());
        gia.setText(str);
        thoigian.setText(list.get(position).getTime());
        return view;
    }
}
