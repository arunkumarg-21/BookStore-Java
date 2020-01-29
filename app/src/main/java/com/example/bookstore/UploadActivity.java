package com.example.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    ImageButton imageView;
    Integer REQUEST_CAMERA=1,SELECT_FILE=0;
    private static  final int PERMISSION_CODE = 1000;
    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = findViewById(R.id.image);


        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TooltipCompat.setTooltipText(v,"choose image");
                onSelectImage();

            }
        });


    }

    public void onSelectImage(){
        final CharSequence[] items={"Camera","Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle("Add Image")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(items[which].equals("Camera")){

                           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                               if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED ||
                                       checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                   String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                   requestPermissions(permission,PERMISSION_CODE);

                               }else{
                                   openCamera();
                               }
                           }else{
                               openCamera();
                           }

                       }else if(items[which].equals("Gallery")){
                           Intent intent = new Intent();
                           intent.setType("image/*");
                           intent.setAction(Intent.ACTION_GET_CONTENT);
                           startActivityForResult(intent.createChooser(intent,"Select Picture"),SELECT_FILE);

                       }else if (items[which].equals("Cancel")){
                           dialog.dismiss();
                       }
                    }

                }).show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        imguri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imguri);
        startActivityForResult(intent,REQUEST_CAMERA);

    }

   // @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CAMERA){

               imageView.setImageURI(imguri);


            }else if(requestCode==SELECT_FILE){

                Uri selectedImageUri = data.getData();
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                    imageView.setImageBitmap(bitmap);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }

}
