package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BaoCao extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Integer> icons = new ArrayList<>();
    List<String> texts = new ArrayList<>();
    //tong so loai san pham hien co, so luong loai san pham da ban, tong so san pham da ban, san pham ban chay nhat
    // doanh thu hoa don cao nhat, tong doanh thu, doanh thu trung binh / hoa don
    ConstraintLayout rootView;
    TextView textView;
    ListView listView;
    ProgressBar process;
    private OnFragmentInteractionListener mListener;

    public BaoCao() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BaoCao newInstance(String param1, String param2) {
        BaoCao fragment = new BaoCao();
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
        rootView = (ConstraintLayout) inflater.inflate(R.layout.fragment_bao_cao, container, false);
        textView = rootView.findViewById(R.id.baocao_textview);
        listView = rootView.findViewById(R.id.baocao_listview);
        process = rootView.findViewById(R.id.baocao_process);
        setBitmap();
        threadGetStatics();
        return rootView;
    }

    public void setBitmap(){
        icons.add(R.drawable.bc_forward_black_24dp);
        icons.add(R.drawable.bc_check_black_24dp);
        icons.add(R.drawable.bc_add_box_black_24dp);
        icons.add(R.drawable.bc_sentiment_very_satisfied_black_24dp);
        icons.add(R.drawable.bc_copyright_black_24dp);
        icons.add(R.drawable.bc_label_outline_black_24dp);
        icons.add(R.drawable.bc_trending_up_black_24dp);
    }

    public void threadGetStatics(){
        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    TaskRequestString taskRequestString = new TaskRequestString();
                    String response = taskRequestString.execute("GET STATICS").get();
                    System.out.println("Response get statics = " + response);
                    JSONObject object = new JSONObject(response);
                    //String str = String.format("%,d", list.get(position).getPrice());
                    texts.add("Tổng sản phẩm đang bán: " + String.format("%,d", object.getInt("totalproduct")));
                    texts.add("Tổng loại sản phẩm đã bán: " + String.format("%,d", object.getInt("totalsoldproduct")));
                    texts.add("Tổng số sản phẩm đã bán: " + String.format("%,d", object.getInt("totalsolditem")));
                    texts.add("Sản phẩm bán chạy nhất: " + object.getString("hotitem"));
                    texts.add("Doanh thu hóa đơn cao nhất: " + String.format("%,d", object.getInt("maxhistory")));
                    texts.add("Tổng doanh thu: " + String.format("%,d", object.getInt("totalhistory")));
                    texts.add("Doanh thu hóa đơn trung bình: " + String.format("%,d", object.getInt("averagehistory")));
                    for (int i = 0; i < texts.size(); i++) {
                        System.out.println(texts.get(i));
                    }
                    final AdapterBC adapterBC = new AdapterBC(getActivity(), R.layout.cellbc, icons, texts);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapterBC);
                            process.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception ex) {
                    ex.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Create.showAlert(getActivity(), "Bạn chưa có giao dịch nào");
                        }
                    });
                }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
