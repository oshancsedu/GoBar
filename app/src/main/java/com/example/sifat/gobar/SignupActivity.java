package com.example.sifat.gobar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sifat.Controller.ServerCommunicator;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 1/30/2016.
 */
public class SignupActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btSignup;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();
    }

    private void init() {


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPass);
        btSignup = (Button) findViewById(R.id.btSignUp);
        btSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSignUp) {
            signup_email = etEmail.getText().toString();
            signup_mobile = etPhone.getText().toString();
            signup_password = etPassword.getText().toString();
            signup_confirmPass = etConfirmPassword.getText().toString();

            if (!signup_password.equals(signup_confirmPass)) {
                showToast(this, "Password did not matched!");
            } else if (!signup_mobile.isEmpty() && !signup_password.isEmpty() && !signup_confirmPass.isEmpty()) {
                ServerCommunicator serverCommunicator = new ServerCommunicator(this);
                serverCommunicator.sendSignUpInfo();
            }
        }
    }
}
