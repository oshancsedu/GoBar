package com.example.sifat.gobar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.sifat.Controller.ServerCommunicator;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import static com.example.sifat.Utilities.CommonUtilities.*;

/**
 * Created by sifat on 1/25/2016.
 */
public class UploadNIDInfoActivity extends ActionBarActivity implements View.OnClickListener{

    EditText etNID;
    Button btSignUp;
    ImageView ivNid;
    private static final int RESULT_TAKE_IMAGE=10002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_nid);
        init();

    }

    private void init() {
        etNID = (EditText) findViewById(R.id.etNidNumber);
        ivNid= (ImageView) findViewById(R.id.ivUploadNid);
        ivNid.setOnClickListener(this);
        btSignUp = (Button) findViewById(R.id.btSignUpToServer);
        btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ivUploadNid)
        {
            Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            gallery.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            startActivityForResult(gallery, RESULT_TAKE_IMAGE);
        }
        else if(v.getId()==R.id.btSignUpToServer)
        {
            signup_NID=etNID.getText().toString();
            if(signup_NID.equalsIgnoreCase(""))
                showToast(this,"Enter Your Nation ID Card Number");
            else
            {
                ServerCommunicator serverCommunicator = new ServerCommunicator(this);
                NDIPic = ((BitmapDrawable) ivNid.getDrawable()).getBitmap();
                serverCommunicator.sendSignupInfo();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK && data != null)
        {
            if(requestCode==RESULT_TAKE_IMAGE)
            {
                Bundle extras= data.getExtras();
                Bitmap source = (Bitmap) extras.get("data");
                //int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth()) / 2;
                int y = (source.getHeight()) / 2;
                ivNid.setImageBitmap(source);
            }
        }
    }
}