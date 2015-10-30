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

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 10/30/2015.
 */
public class ProfileActivity extends ActionBarActivity{

    private ImageView profilePic;
    private SharedPreferences sharedPreferences;
    private TextView tvUserName;
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
        String profileImgUrl = "https://graph.facebook.com/968191129909623/picture?type=large";
        Glide.with(this).load(profileImgUrl).into(profilePic);
        tvUserName=(TextView)findViewById(R.id.tvUserName);
        tvUserName.setText(sharedPreferences.getString(USER_NAME,"Not Found"));
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
