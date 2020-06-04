package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SanPham.OnFragmentInteractionListener, LichSu.OnFragmentInteractionListener, BaoCao.OnFragmentInteractionListener, QuanLyKho.OnFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(Home.this, AddProduct.class);
                startActivity(intent);
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setDisplay();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SanPham()).commit();
            navigationView.setCheckedItem(R.id.home_sanpham);
        }
    }

    public void setDisplay(){
        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);
        TextView username = header.findViewById(R.id.home_username);
        TextView email = header.findViewById(R.id.home_email);
        username.setText("Xin chào, " + MainActivity.loginUser.getUsername());
        email.setText(MainActivity.loginUser.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_lammoi:
                int n = navigationView.getCheckedItem().getItemId();
                switch (n) {
                    case R.id.home_sanpham:
                        System.out.println("San pham");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SanPham()).commit();
                        break;
                    case R.id.home_lichsugiaodich:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new LichSu()).commit();
                        navigationView.setCheckedItem(R.id.home_lichsugiaodich);
                        break;
                    case R.id.home_baocao:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new BaoCao()).commit();
                        navigationView.setCheckedItem(R.id.home_baocao);
                        break;
                    case R.id.home_kho:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new QuanLyKho()).commit();
                        navigationView.setCheckedItem(R.id.home_kho);
                        break;
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_lichsugiaodich:
                System.out.println("Lick su giao dich");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LichSu()).commit();
                navigationView.setCheckedItem(R.id.home_lichsugiaodich);
                break;
            case R.id.home_baocao:
                System.out.println("Bao cao");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BaoCao()).commit();
                navigationView.setCheckedItem(R.id.home_baocao);
                break;
            case R.id.home_kho:
                System.out.println("Kho");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new QuanLyKho()).commit();
                navigationView.setCheckedItem(R.id.home_kho);
                break;
            case R.id.home_sanpham:
                System.out.println("San pham");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SanPham()).commit();
                navigationView.setCheckedItem(R.id.home_sanpham);
                break;
            case R.id.home_dangxuat:
                MainActivity.loginUser = null;
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //static Fragment[] fragments = {null, null, null, null, null};
    //static int tagFra;

}
