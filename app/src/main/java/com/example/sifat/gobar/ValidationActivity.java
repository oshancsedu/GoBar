package com.example.sifat.gobar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.example.sifat.Domain.FacebookInfo;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by sifat on 10/31/2015.
 */
public class ValidationActivity extends ActionBarActivity {

    private Bundle bundle;
    private FacebookInfo facebookInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        init();
        bundle=this.getIntent().getExtras();
        facebookInfo= (FacebookInfo) bundle.getSerializable(USER_FB_INFO);
        Toast.makeText(this,facebookInfo.getAddress()+facebookInfo.getBday(),Toast.LENGTH_SHORT).show();

    }

    private void init() {
        bundle=new Bundle();
    }
}
