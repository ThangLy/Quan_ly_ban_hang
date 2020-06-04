package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SanPham extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ConstraintLayout rootView;
    ListView listView;
    TextView tongcong;
    int count;
    List<Item> listItem = new ArrayList<>();
    AdapterSP adapterXHD;
    ProgressBar process;

    public SanPham() {

    }


    // TODO: Rename and change types and number of parameters
    public static SanPham newInstance(String param1, String param2) {
        SanPham fragment = new SanPham();
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
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_san_pham, container, false);
        tongcong = rootView.findViewById(R.id.xuathoadon_tongcong);
        listView = rootView.findViewById(R.id.xuathoadon_listview);
        process = rootView.findViewById(R.id.sanpham_process);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                Item item = (Item)listView.getAdapter().getItem(position);
                clickOn(item);
            }
        });
        threadRequestListItem();
        return rootView;
    }

    public void clickOn(final Item item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bán sản phẩm");
        builder.setMessage("Nhập số lượng để bán");
        final EditText input = new EditText(getActivity());
        input.setSingleLine();
        FrameLayout container = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.inputalertdialog);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.inputalertdialog);
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String inputText = input.getText().toString();
                    int amount = Integer.parseInt(inputText);
                    if (amount > item.getQuanlity())
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Create.showAlert(getActivity(), "Số lượng bán không được nhiều hơn số lượng trong kho");
                            }
                        });
                    }
                    else {
                        JSONObject obj = new JSONObject();
                        obj.put("id", item.getId());
                        obj.put("amount", amount);
                        obj.put("price", item.getPrice());
                        obj.put("quanlity", item.getQuanlity());
                        TaskRequestString taskRequestString = new TaskRequestString();
                        String response = taskRequestString.execute("SELL|" + obj.toString()).get();
                        if (response.equals("true")) {
                            Toast.makeText(getContext(), "Bán thành công " + item.getName() + ". Số lượng: " + amount + "\nSố lượng còn lại trong kho: " + (item.getQuanlity() - amount), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    Toast.makeText(getContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
            adapterXHD = new AdapterSP(getActivity(), R.layout.cellsp, listItem);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(adapterXHD);
                    tongcong.setText("Tổng cộng: " + adapterXHD.getCount());
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
