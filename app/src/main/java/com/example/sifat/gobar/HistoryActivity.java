package com.example.sifat.gobar;

import android.content.Context;
import android.content.Intent;
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

import static com.example.sifat.Utilities.CommonUtilities.Logout;

/**
 * Created by sifat on 1/27/2016.
 */
public class HistoryActivity extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private  DrawerLayout dlMenu;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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
