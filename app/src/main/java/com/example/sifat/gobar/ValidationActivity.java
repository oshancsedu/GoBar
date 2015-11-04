package com.example.sifat.gobar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sifat.Controller.SendingSignupInfo;
import com.example.sifat.Domain.FacebookInfo;

import java.util.ArrayList;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by sifat on 10/31/2015.
 */
public class ValidationActivity extends ActionBarActivity implements View.OnClickListener {


    private EditText etFirstName,etLastName,etAddress,etPhoneNumber,etEmail,etBday;
    private Button btSignup;
    private Spinner spGender;
    private Bundle bundle;
    private String fname,lname,bday,address,email,mobile,password,gender;
    private FacebookInfo facebookInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        init();

        etFirstName.setText(facebookInfo.getFirstName());
        etLastName.setText(facebookInfo.getLastName());
        etEmail.setText(facebookInfo.getEmail());
        etAddress.setText(facebookInfo.getAddress());
        etBday.setText(facebookInfo.getBday());
        spGender.setSelection(facebookInfo.getGender());
        Toast.makeText(this,facebookInfo.getAddress()+facebookInfo.getBday(),Toast.LENGTH_SHORT).show();

    }

    private void init() {
        bundle=new Bundle();
        bundle=this.getIntent().getExtras();
        facebookInfo= (FacebookInfo) bundle.getSerializable(USER_FB_INFO);
        etFirstName=(EditText)findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etBday=(EditText)findViewById(R.id.etBday);
        etAddress=(EditText)findViewById(R.id.etAddress);
        spGender= (Spinner) findViewById(R.id.gender);
        etPhoneNumber=(EditText)findViewById(R.id.etPhoneNumber);
        btSignup= (Button)findViewById(R.id.btSignUp);
        btSignup.setOnClickListener(this);

        ArrayList<String> genderArray = new ArrayList<String>();
        genderArray.add("-Gender-");
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Other");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, genderArray);
        spGender.setAdapter(spinnerArrayAdapter);

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show();
        fname=etFirstName.getText().toString();
        lname=etLastName.getText().toString();
        bday=etBday.getText().toString();
        address=etAddress.getText().toString();
        email=etEmail.getText().toString();
        mobile=etPhoneNumber.getText().toString();
        password="1234";
        gender=spGender.getSelectedItem().toString();
        SendingSignupInfo sendingSignupInfo = new SendingSignupInfo(this);
        sendingSignupInfo.sendSignupInfo(fname,lname,address,bday,gender,password,email,mobile);

    }
}