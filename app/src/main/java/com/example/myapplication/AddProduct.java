package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddProduct extends AppCompatActivity {

    ImageView hinhsp;
    EditText masp;
    EditText tensp;
    EditText giasp;
    EditText soluongsp;
    TextView xoahinh;
    Bitmap defaultBitmap;
    Bitmap imageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setupUI(findViewById(R.id.addproduct_addproduct));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar1));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        hinhsp = findViewById(R.id.addproduct_image);
        masp = findViewById(R.id.addproduct_id);
        tensp = findViewById(R.id.addproduct_name);
        giasp = findViewById(R.id.addproduct_price);
        soluongsp = findViewById(R.id.addproduct_quanlity);
        xoahinh = findViewById(R.id.addproduct_textview);
        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagehere);
        hinhsp.setImageBitmap(defaultBitmap);
        imageSelected = defaultBitmap;
        xoahinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hinhsp.setImageBitmap(defaultBitmap);
            }
        });

        hinhsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

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
                    hideSoftKeyboard(AddProduct.this);
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

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                imageSelected = bm;
                hinhsp.setImageBitmap(bm);
            }catch (Exception de){
                Toast.makeText(AddProduct.this, de.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.menuaddproduct, menu);
        return true;
    }

    public boolean check(){
        String ma = masp.getText().toString();
        String ten = tensp.getText().toString();
        String gia = giasp.getText().toString();
        String soluong = soluongsp.getText().toString();
        if (ma.length() != 4) {
            Create.showAlert(AddProduct.this, "Mã sản phẩm phải là duy nhất & có 4 ký tự");
            return false;
        }
        else if (ten.trim().length() == 0) {
            Create.showAlert(AddProduct.this, "Tên sản phẩm không được bỏ trống");
            return false;
        }
        else
        {
            try {
                int n = Integer.parseInt(gia);
                int m = Integer.parseInt(soluong);
                if (n <= 0 || m <= 0) {
                    Create.showAlert(AddProduct.this, "Giá & số lượng phải là số nguyên lớn hơn 0");
                    return false;
                }
                else
                    return true;
            }catch (Exception ex){
                ex.printStackTrace();
                Create.showAlert(AddProduct.this, "Giá & số lượng phải là số nguyên lớn hơn 0");
                return false;
            }
        }
    }

    boolean adding = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.addproduct_add:
                if (!adding) {
                    System.out.println("Thêm");
                    if (check()) {
                        adding = true;
                        final String ma = masp.getText().toString();
                        final String ten = tensp.getText().toString();
                        final String gia = giasp.getText().toString();
                        final String soluong = soluongsp.getText().toString();
                        if (imageSelected.getWidth() > 200) {
                            int height = imageSelected.getHeight() * 200 / imageSelected.getWidth();
                            Bitmap newbitmap = Bitmap.createScaledBitmap(imageSelected, 200, height, false);
                            imageSelected = newbitmap;
                        }
                        addProductToServer(ma, ten, gia, soluong);
                    }
                }
                else {
                    Create.showAlert(this, "Hãy chờ thêm sản phẩm trước hoàn thành");
                }
                return true;
            default:
                return true;
        }
    }

    public void addProductToServer(final String id, final String ten, final String gia, final String soluong){
        Thread thread = new Thread() {
            @Override
            public void run(){
                try {
                    String response = upload(imageSelected, id + "|" + ten + "|" + gia + "|" + soluong, "hinhsanpham.jpg", 100);
                    System.out.println("Response add product = " + response);
                    if (response.equals("true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Create.showAlert(AddProduct.this, "Thêm sản phẩm mới thành công");
                            }
                        });
                    }
                    else {
                        if (response.equals("dup")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Create.showAlert(AddProduct.this, "Trùng mã sản phẩm");
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Create.showAlert(AddProduct.this, "Lỗi kết nối");
                                }
                            });
                        }
                    }
                }catch (Exception ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Create.showAlert(AddProduct.this, "Xảy ra lỗi");
                        }
                    });
                    ex.printStackTrace();
                }
                adding = false;
            }
        };
        thread.start();
    }

    //upload(bitmap, "timeline",MainActivity.loginStudent.getMssv() + ".jpg", 25);

    public static String upload(Bitmap bm, String name, String fileName, int quality) {
        try {
            URL url = new URL("http://vkapp.info/test/Default.aspx");
            String boundary = "*****";
            String lnEnd = "\r\n";
            String dbHyphen = "--";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.connect();
            OutputStream os = conn.getOutputStream();

            os.write((dbHyphen + boundary + lnEnd).getBytes());
            os.write(("Content-Disposition: form-data; name=\"p\"" + lnEnd).getBytes());
            os.write(("Content-Type: text/plain; charset=UTF-8" + lnEnd).getBytes());
            os.write(lnEnd.getBytes());
            os.write(lnEnd.getBytes());
            os.write((dbHyphen + boundary + lnEnd).getBytes());
            os.write(("Content-Disposition: form-data; name=\"" + name + "\";filename=" + fileName + lnEnd).getBytes());
            os.write(("Content-Type: image/jpeg; charset=UTF-8" + lnEnd).getBytes());
            os.write((lnEnd).getBytes());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            byte[] bitmapdata = bos.toByteArray();
            os.write(bitmapdata);
            os.write((lnEnd).getBytes());
            os.write((dbHyphen + boundary + dbHyphen + lnEnd).getBytes());
            os.close();
            InputStream responseStream = new
                    BufferedInputStream(conn.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();
            String response = stringBuilder.toString();
            if (response.contains("\n")) {
                String[] p = response.split("\n");
                if (p.length <= 2)
                    response = p[0];
            }
            //System.out.println("Response = " + response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
