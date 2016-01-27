package com.example.sifat.gobar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 10/30/2015.
 */
public class ProfileActivity extends ActionBarActivity{

    private ImageView profilePic;
    private SharedPreferences sharedPreferences;
    private TextView tvUserName,tvAddress,tvEmail,tvMobile,tvProfession,tvBalance;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(String.valueOf(R.string.sharedPref), this.MODE_PRIVATE);
        profilePic= (ImageView) findViewById(R.id.ivProfilepic);

        Picasso.with(this).load(profileImgUrl).resize(200,200).centerCrop().into(profilePic);
        tvUserName=(TextView)findViewById(R.id.tvUserName);
        tvAddress=(TextView)findViewById(R.id.tvAddrss);
        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvMobile=(TextView)findViewById(R.id.tvPhone);
        tvBalance=(TextView)findViewById(R.id.tvBalance);
        tvProfession=(TextView)findViewById(R.id.tvProfession);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        Intent p;
        switch (item.getItemId()) {

            case R.id.history:
                break;

            case R.id.logout:
                Logout(this);
                finish();
                break;
        }

        return false;
    }
}
