package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.IpSecManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText username;
    EditText password;
    Button button;
    ProgressBar process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI(findViewById(R.id.login_login));
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        button = findViewById(R.id.login_OK);
        process = findViewById(R.id.login_process);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangnhap();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Đăng nhập");
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar1));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


    public void dangnhap(){
        String user = username.getText().toString().trim();
        String pass = password.getText().toString();
        if (user.length() >= 4 && user.length() <= 30) {
            if (pass.length() > 3 && pass.length() <= 30) {
                login(user, pass);
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Create.showAlert(Login.this, "Mật khẩu không đúng");
                    }
                });
            }
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Create.showAlert(Login.this, "Tên tài khoản không đúng");
                }
            });
        }
    }

    public void login(String userxx, String passxx){
        process.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        final String user = userxx;
        final String pass = passxx;
        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("username", user);
                    obj.put("password", pass);
                    TaskRequestString taskRequestString = new TaskRequestString();
                    final String response = taskRequestString.execute("LOGIN|" + obj.toString()).get();
                    System.out.println("Response login = " + response);
                    if (response.contains("{")) {
                        System.out.println("Login thanh cong");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject data = new JSONObject(response);
                                    String id = data.getString("username");
                                    String ps = data.getString("password");
                                    String mail = data.getString("email");
                                    MainActivity.loginUser = new User(id, ps, mail);
                                    button.setEnabled(true);
                                    process.setVisibility(View.GONE);
                                    Intent intent = new Intent(Login.this, Home.class);
                                    startActivity(intent);
                                }catch (Exception xx)
                                {
                                    xx.printStackTrace();
                                }
                            }
                        });
                    }
                    else {
                        if (response.equals("wrong")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setEnabled(true);
                                    process.setVisibility(View.GONE);
                                    Create.showAlert(Login.this, "Tài khoản không chính xác");
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    button.setEnabled(true);
                                    process.setVisibility(View.GONE);
                                    Create.showAlert(Login.this, "Tài khoản không tồn tại");
                                }
                            });

                        }
                    }
                }catch (Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setEnabled(true);
                            process.setVisibility(View.GONE);
                        }
                    });
                    ex.printStackTrace();
                }
            }
        };
        thread.start();

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Login.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}
