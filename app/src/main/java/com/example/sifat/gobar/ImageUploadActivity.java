package com.example.sifat.gobar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sifat.Controller.ServerCommunicator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import static com.example.sifat.Utilities.CommonUtilities.*;
/**
 * Created by sifat on 1/23/2016.
 */
public class ImageUploadActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btChooseImage,btTakeImage,btNext;
    private ImageView ivProfilePic;
    private static final int RESULT_LODE_IMAGE=10001;
    private static final int RESULT_TAKE_IMAGE=10002;
    private boolean isPictureTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);
        init();
    }

    private void init() {
        isPictureTaken=false;
        ivProfilePic = (ImageView) findViewById(R.id.ivUploadProfilePic);
        btChooseImage = (Button) findViewById(R.id.btUploadFromGallery);
        btTakeImage = (Button) findViewById(R.id.btTakePhoto);
        btNext = (Button) findViewById(R.id.btNextToNID);
        btChooseImage.setOnClickListener(this);
        btTakeImage.setOnClickListener(this);
        btNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btUploadFromGallery){
            Intent gallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            /*File imageDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String path = imageDirectory.getPath();
            Uri data = Uri.parse(path);
            gallery.setDataAndType(data,"image/*");*/
            startActivityForResult(gallery,RESULT_LODE_IMAGE);
        }
        else if(v.getId()==R.id.btTakePhoto)
        {
            Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(gallery,RESULT_TAKE_IMAGE);
        }
        else if(v.getId()==R.id.btNextToNID)
        {
            showToast(this,"CLICKED");
            proPic = ((BitmapDrawable) ivProfilePic.getDrawable()).getBitmap();

            Intent nextIntent= new Intent(this,UploadNIDInfoActivity.class);
            startActivity(nextIntent);

            //BitmapFactory.Options options=new BitmapFactory.Options();
            //options.inSampleSize = 8;
            //image = BitmapFactory.decodeStream(image,null,options);
            //serverCommunicator.sendImage();
            //serverCommunicator.sendSignupInfo(signup_fname,signup_lname,signup_address,signup_bday,signup_gender,signup_password,signup_email,signup_mobile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == RESULT_LODE_IMAGE) {
                Uri selectedImages = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImages);
                    //ivProfilePic.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                    Picasso.with(this).load(selectedImages).resize(200, 200).centerCrop().into(ivProfilePic);
                    isPictureTaken=true;
                } catch (Exception e) {
                    showToast(this, "Problem in loading image");
                }
            } else if (requestCode == RESULT_TAKE_IMAGE) {
                Bundle extras = data.getExtras();
                Bitmap source = (Bitmap) extras.get("data");
                int size = Math.min(source.getWidth(), source.getHeight());
                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;
                ivProfilePic.setImageBitmap(Bitmap.createBitmap(source, x, y, size, size));
                isPictureTaken=true;
            }
        }
    }
}
