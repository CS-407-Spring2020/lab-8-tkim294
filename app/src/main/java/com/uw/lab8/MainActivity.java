package com.uw.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.DrmInitData;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button downloadButton = (Button) findViewById(R.id.download);
        Button uploadButton = (Button) findViewById(R.id.upload);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageDrawable(null);
    }

    public void downloadFunction(View view) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference simpsonRef = storageReference.child("images/simpson.jpg");

            final ImageView imageView = findViewById(R.id.imageView);
            final long ONE_MEGABYTE = 1024 * 1024;

            simpsonRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Log.i("Error", "Image Download failed.");
                }
            });
    }

    public void uploadFunction(View view) {
        try {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference simpsonRef = storageRef.child("images/simpson.jpg");

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.simpson);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bartByteSteram = baos.toByteArray();

            UploadTask uploadTask = simpsonRef.putBytes(bartByteSteram);
            uploadTask.addOnFailureListener((exception) -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("ImageUpload", "Image successfully uploaded to FIrebase.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Error", "Image upload failed.");
        }
    }
}
