package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONObject;

public class Create extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText email;
    Button buttonOK;
    ProgressBar process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setupUI(findViewById(R.id.create_create));
        username = findViewById(R.id.create_id);
        password = findViewById(R.id.create_password);
        email = findViewById(R.id.create_email);
        buttonOK = findViewById(R.id.create_ok);
        process = findViewById(R.id.create_process);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangky();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tạo tài khoản");
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

    public void dangky(){
       String user = username.getText().toString().trim();
       String pass = password.getText().toString();
       String em = email.getText().toString();

        if (user.length() >= 4 && user.length() <= 30) {
            if (pass.length() > 3) {
                if (em.length() > 3 && em.contains("@")) {
                    yeucau(user, pass, em);
                }
                else {
                    showAlert(this, "Email không hợp lệ");
                }
            }
            else {
                showAlert(this, "Mật khẩu từ 4 - 30 ký tự");
            }
        }
        else {
            showAlert(this, "Tên tài khoản từ 4 - 30 ký tự");
        }
    }

    public void yeucau(String u, String p, String e){
        process.setVisibility(View.VISIBLE);
        buttonOK.setEnabled(false);
        final String user = u;
        final String pass = p;
        final String em = e;
        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("username", user);
                    obj.put("password", pass);
                    obj.put("email", em);
                    System.out.println(obj.toString());
                    TaskRequestString taskRequestString = new TaskRequestString();
                    String response = taskRequestString.execute("ADD ACCOUNT|" + obj.toString()).get();
                    System.out.println("Response Add account = " + response);
                    if (response.equals("true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                process.setVisibility(View.GONE);
                                buttonOK.setEnabled(true);
                                showAlert(Create.this, "Đăng ký thành công");
                            }
                        });

                        //
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                process.setVisibility(View.GONE);
                                buttonOK.setEnabled(true);
                                showAlert(Create.this, "Tên tài khoản đã tồn tại");
                            }
                        });

                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                    process.setVisibility(View.GONE);
                    buttonOK.setEnabled(true);
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
                    hideSoftKeyboard(Create.this);
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

    public static void showAlert(Activity act, String str){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(act);
            builder.setMessage(str)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
