package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class HoaDon extends AppCompatActivity {

    TextView magd;
    TextView tensp;
    TextView masp;
    TextView soluongsp;
    TextView dongia;
    TextView tongtien;
    TextView thoigian;
    ImageView hinhsp;
    Button button;
    public static History history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lịch sử giao dịch");
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar1));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        magd = findViewById(R.id.hoadon_magd);
        tensp = findViewById(R.id.hoadon_tensp);
        masp = findViewById(R.id.hoadon_masp);
        soluongsp = findViewById(R.id.hoadon_soluongsp);
        dongia = findViewById(R.id.hoadon_giasp);
        tongtien = findViewById(R.id.hoadon_tongtien);
        thoigian = findViewById(R.id.hoadon_thoigian);
        hinhsp = findViewById(R.id.hoadon_hinhsp);
        button = findViewById(R.id.hoadon_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HoaDon.this);
                    builder.setMessage("Xuất hóa đơn thành công")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        setText();
    }

    public void setText(){
        magd.setText("Mã hóa đơn: " + history.getCode());
        tensp.setText("Tên sản phẩm: " + history.getItem().getName());
        masp.setText("Mã sản phẩm: " + history.getItem().getId());
        String soluong = String.format("%,d", history.getAmount());
        soluongsp.setText("Số lượng bán: " + soluong);
        String donvi = String.format("%,d", history.getItem().getPrice());
        dongia.setText("Đơn giá: " + donvi);
        String totalcost = String.format("%,d", history.getTotal());
        tongtien.setText("Tổng tiền: " + totalcost);
        thoigian.setText("Thời gian: " + history.getTime());
        hinhsp.setImageBitmap(history.getItem().getImage());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }
}
