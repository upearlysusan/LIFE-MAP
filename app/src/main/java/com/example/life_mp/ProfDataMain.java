package com.example.life_mp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfDataMain extends AppCompatActivity {
    private Button add;
    private Button view;
    private Uri imageUri;
    private String url = null;
    private StorageReference mStorage;
    private FirebaseFirestore db;
    String uid;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_prof_data_main );
        add=findViewById( R.id.add );
        view=findViewById( R.id.view );
        mStorage= FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if (checkSelfPermission( Manifest.permission.CAMERA )== PackageManager.PERMISSION_DENIED || checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE )==PackageManager.PERMISSION_DENIED){
                        String [] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions( permission,CAMERA_PERM_CODE );
                    }
                    else{openCamera();}
                }
                else{openCamera();}



            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfDataMain.this,MemberDataMain.class);
                startActivity(intent);

            }
        });

    }
    private void openCamera() {
        ContentValues values=new ContentValues(  );
        values.put( MediaStore.Images.Media.TITLE,"NEW PICTURE");
        imageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent=new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        cameraIntent.putExtra( MediaStore.EXTRA_OUTPUT,imageUri );
        startActivityForResult( cameraIntent,CAMERA_REQUEST_CODE );

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_PERM_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else{
                    Toast.makeText(ProfDataMain.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (resultCode == RESULT_OK) {
            if(imageUri != null){
                uid = System.currentTimeMillis() + "." + getFileExtension(imageUri);
                final StorageReference filepath=mStorage.child("uploads").child(uid);
                filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url = uri.toString();
                                //uid = System.currentTimeMillis() + "." + getFileExtension(imageUri);
                                Log.d("DownloadUrl", url);
                                pd.dismiss();
                                Toast.makeText(ProfDataMain.this, "Upload Successful" + url, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });}
            else{Toast.makeText(ProfDataMain.this, "Upload Successful" + url, Toast.LENGTH_SHORT).show();}

        }
    }
    private String getFileExtension (Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}