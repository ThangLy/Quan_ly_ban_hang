package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class QuanLyKho extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    AdapterKho adapterKho;
    List<Item> listItem  = new ArrayList<>();
    int count;
    ListView listView;
    ConstraintLayout rootView;
    ProgressBar process;


    public QuanLyKho() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static QuanLyKho newInstance(String param1, String param2) {
        QuanLyKho fragment = new QuanLyKho();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_quan_ly_kho, container, false);
        listView = rootView.findViewById(R.id.kho_listview);
        process = rootView.findViewById(R.id.kho_process);
        threadRequestListItem();
        return rootView;
    }

    public void threadRequestListItem(){
        Thread thread = new Thread() {
            @Override
            public  void run(){
                requestListItem();
            }
        };
        thread.start();
    }

    public synchronized void requestListItem(){
        try {
            TaskRequestString taskRequestString = new TaskRequestString();
            String response = taskRequestString.execute("GET LIST PRODUCT").get();
            System.out.println("Response list item = " + response);
            JSONArray array = new JSONArray(response);
            count = 0;
            //Item(String id, String name, Bitmap image, int price)
            for (int i = 0; i < array.length(); i++)
            {
                final JSONObject obj = array.getJSONObject(i);
                threadLoadSanPham(obj);
            }
            int timeout = 10;
            while (array.length() != count) {
                Thread.sleep(1000);
                --timeout;
                if (timeout == 0)
                    break;
            }
            adapterKho = new AdapterKho(getActivity(), R.layout.cellsp, listItem);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(adapterKho);
                    process.setVisibility(View.GONE);
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
            process.setVisibility(View.GONE);
        }
    }

    public void threadLoadSanPham(final JSONObject obj) {
        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    Bitmap bitmap = MainActivity.getBitmapFromURL("http://vkapp.info/test/hinhsanpham/" + obj.getString("image"));
                    Item item = new Item(obj.getString("id"), obj.getString("name"), bitmap, obj.getInt("price"), obj.getInt("quanlity"));
                    listItem.add(item);
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
                ++count;
            }
        };
        thread.start();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
