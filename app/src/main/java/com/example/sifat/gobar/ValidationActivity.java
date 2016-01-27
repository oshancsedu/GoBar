package com.example.sifat.gobar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sifat.Controller.ServerCommunicator;
import com.example.sifat.Domain.FacebookInfo;

import java.util.ArrayList;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 10/31/2015.
 */
public class ValidationActivity extends ActionBarActivity implements View.OnClickListener {


    private EditText etFirstName,etLastName,etAddress,etPhoneNumber,etEmail,etBday,etProfession;
    private Button btSignup;
    private Spinner spGender;
    private Bundle bundle;
    private FacebookInfo facebookInfo;
    private LinearLayout llPassword, llConfirmPass;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        init();

        llPassword = (LinearLayout) findViewById(R.id.llPasswordPanel);
        llPassword.setVisibility(View.GONE);
        llConfirmPass = (LinearLayout) findViewById(R.id.llConfirmPasswordPanel);
        llConfirmPass.setVisibility(View.GONE);
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
        etProfession= (EditText) findViewById(R.id.etProfession);
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
        signup_fname=etFirstName.getText().toString();
        signup_lname=etLastName.getText().toString();
        signup_bday=etBday.getText().toString();
        signup_address=etAddress.getText().toString();
        signup_email=etEmail.getText().toString();
        signup_profession = etProfession.getText().toString();
        signup_mobile=etPhoneNumber.getText().toString();
        signup_password = "";
        signup_gender=spGender.getSelectedItem().toString();

        if ( !signup_profession.isEmpty() && !signup_fname.isEmpty() && !signup_lname.isEmpty() &&
                !signup_bday.isEmpty() && !signup_address.isEmpty() && !signup_email.isEmpty()
                && !signup_mobile.isEmpty() && !signup_gender.equalsIgnoreCase("-Gender-"))
        {
            intent = new Intent(ValidationActivity.this,ImageUploadActivity.class);
            startActivity(intent);
        }
        else
            showToast(this, "Fill up all the field");
    }
}