package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    ImageView hinhapp;
    Button buttonDangky;
    Button buttonDangnhap;
    TextView quenMatkhau;
    ProgressBar process;
    public static User loginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hinhapp = findViewById(R.id.main_imageview);
        buttonDangky = findViewById(R.id.main_buttondangky);
        buttonDangnhap = findViewById(R.id.main_buttondangnhap);
        quenMatkhau = findViewById(R.id.main_textviewquenmatkhau);
        process = findViewById(R.id.main_process);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Comfy speed");
        // hinhapp.setImageResource(R.drawable.hinhne);

        buttonDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateActivity();
            }
        });

        buttonDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });

        quenMatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

    }

    public void openCreateActivity(){
        Intent intent = new Intent(this, Create.class);
        startActivity(intent);
    }
    public void openLoginActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    public void forgotPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tìm lại mật khẩu");
        builder.setMessage("Nhập tên tài khoản");
        final EditText input = new EditText(this);
        input.setSingleLine();
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.inputalertdialog);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.inputalertdialog);
        input.setLayoutParams(params);
        container.addView(input);
        builder.setView(container);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            process.setVisibility(View.VISIBLE);
            quenMatkhau.setEnabled(false);
            final String inputText = input.getText().toString();
            Thread thread = new Thread() {
                @Override
                public void run(){
                    requestForgotPassword(inputText);
                }
            };
            thread.start();
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

    public void requestForgotPassword(String user){
        try {
            JSONObject obj = new JSONObject();
            obj.put("username", user);
            TaskRequestString taskRequestString = new TaskRequestString();
            String response = taskRequestString.execute("FORGOT PASSWORD|" + obj.toString()).get();
            System.out.println("Response forgot: " + response);
            if (response.equals("true")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Create.showAlert(MainActivity.this, "Mật khẩu đã được gửi về email của bạn");
                    }
                });
            }
            else {
                if (response.equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Create.showAlert(MainActivity.this, "Xảy ra lỗi kết nối");
                        }
                    });
                }
                else if (response.equals("not exists")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Create.showAlert(MainActivity.this, "Tài khoản không tồn tại");
                        }
                    });
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Create.showAlert(MainActivity.this, "Lỗi khi gửi email");
                        }
                    });
                }
            }
        }catch (Exception ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Create.showAlert(MainActivity.this, "Xảy ra lỗi kết nối");
                }
            });
            ex.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                process.setVisibility(View.GONE);
                quenMatkhau.setEnabled(true);
            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

}
