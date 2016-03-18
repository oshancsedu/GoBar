package com.example.sifat.gobar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 10/30/2015.
 */
public class ProfileActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView profilePic;
    private SharedPreferences sharedPreferences;
    private TextView tvUserName,tvAddress,tvEmail,tvMobile,tvProfession,tvBalance,tvGender,tvBday;
    private Toolbar toolbar;
    private String profilePicURL,mobileNumber,email,profession,gender,fname,lname,address,bDay;
    private float rating,balance;
    private RatingBar rbUserRatingBar;
    private DrawerLayout dlMenu;
    private NavigationView navView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_action_back);
        ab.setDisplayHomeAsUpEnabled(true);

        //dlMenu = (DrawerLayout) findViewById(R.id.drawer);

        /*navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(this);*/

        sharedPreferences = getSharedPref(this);
        profilePicURL = sharedPreferences.getString(USER_PRO_PIC_URL, "");
        rating = Float.parseFloat(sharedPreferences.getString(USER_RATING, "0.0"));
        balance = Float.parseFloat(sharedPreferences.getString(USER_BALANCE, "0.0"));
        email = sharedPreferences.getString(USER_EMAIL, "");
        address = sharedPreferences.getString(USER_ADDRESS, "");
        gender = sharedPreferences.getString(USER_GENDER, "");
        mobileNumber = sharedPreferences.getString(USER_MOBILE_NUM, "");
        fname = sharedPreferences.getString(USER_FNAME, "");
        lname = sharedPreferences.getString(USER_LNAME, "");
        profession = sharedPreferences.getString(USER_PROFESSION, "");
        bDay = sharedPreferences.getString(USER_BDAY, "");

        profilePic= (ImageView) findViewById(R.id.ivProfilepic);
        Picasso.with(this).load(profilePicURL).resize(200,200).centerCrop().into(profilePic);

        tvUserName=(TextView)findViewById(R.id.tvUserName);
        tvUserName.setText(fname + " " + lname);

        tvAddress=(TextView)findViewById(R.id.tvAddress);
        tvAddress.setText(address);

        tvEmail=(TextView)findViewById(R.id.tvEmail);
        tvEmail.setText(email);

        tvMobile=(TextView)findViewById(R.id.tvPhone);
        tvMobile.setText(mobileNumber);

        tvBalance=(TextView)findViewById(R.id.tvBalance);
        tvBalance.setText("Your Balance is "+balance+" taka");

        tvProfession=(TextView)findViewById(R.id.tvProfession);
        tvProfession.setText(profession);

        rbUserRatingBar = (RatingBar) findViewById(R.id.rbUserRate);
        rbUserRatingBar.setRating(rating + 0.5f);
        rbUserRatingBar.setRating(rating);

        tvBday = (TextView) findViewById(R.id.tvBirthday);
        tvBday.setText(bDay);

        tvGender = (TextView) findViewById(R.id.tvGender);
        tvGender.setText(gender);
    }

    /******
     * Menu Settings
     ****/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuNavigation(item,this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return menuNavigation(item,this);
    }

    private boolean menuNavigation(MenuItem item,Context context)
    {
        Intent intent;
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;

            /*case R.id.navigation_item_map:
                intent = new Intent(context,MapsActivity.class);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                startActivity(intent);
                break;

            case R.id.navigation_item_profile:
                intent = new Intent(context,ProfileActivity.class);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                startActivity(intent);
                break;

            case R.id.navigation_item_logout:
                Logout(this);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                finish();
                break;

            case R.id.navigation_item_about:
                intent = new Intent(context,AboutActivity.class);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                startActivity(intent);
                break;

            case R.id.navigation_item_help:
                intent = new Intent(context,HelpActivity.class);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                startActivity(intent);
                break;

            case R.id.navigation_item_history:
                intent = new Intent(context,HistoryActivity.class);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                startActivity(intent);
                break;

            case R.id.navigation_item_settings:
                intent = new Intent(context,SettingsActivity.class);
                if(dlMenu.isDrawerOpen(GravityCompat.START))
                    dlMenu.closeDrawers();
                startActivity(intent);
                break;*/
        }
        return false;
    }
}