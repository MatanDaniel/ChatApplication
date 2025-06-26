package com.example.chatapplication.views;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityUpdateProfileBinding;
import com.example.chatapplication.models.ApiClient;
import com.example.chatapplication.models.UserApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//TODO: WE DID NOT IMPLEMENT CORRECTLY THE IMAGE TO BE SAVED AFTER THE PROFILE IS EDITED
public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    ActivityUpdateProfileBinding updateProfileBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateProfileBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(updateProfileBinding.getRoot());

        // Initialize the image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        updateProfileBinding.imageViewProfielUpdateProfile.setImageURI(selectedImageUri);
                    }
                }
        );

        // Make the image clickable to open gallery
        updateProfileBinding.imageViewProfielUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });


        updateProfileBinding.buttonUpdateProfile.setOnClickListener(v -> {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfileBinding.progressBarUpdateProfile.setVisibility(View.VISIBLE);

            MultipartBody.Part imagePart = prepareFilePart(selectedImageUri, "image");
            RequestBody userIdBody = RequestBody.create(MultipartBody.FORM, "1"); // Replace "1" with real userId later

            UserApi userApi = ApiClient.getClient().create(UserApi.class);
            Call<ResponseBody> call = userApi.uploadProfilePicture(userIdBody, imagePart);

            call.enqueue(new Callback<ResponseBody>() {
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    updateProfileBinding.progressBarUpdateProfile.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        Toast.makeText(UpdateProfileActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();

                        // ✅ Navigate to MainActivity
                        Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: close UpdateProfileActivity so user can't go back to it with back button

                    } else {
                        Toast.makeText(UpdateProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    updateProfileBinding.progressBarUpdateProfile.setVisibility(View.GONE);
                    Toast.makeText(UpdateProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

    }




    private MultipartBody.Part prepareFilePart(Uri fileUri, String partName) {
        File file = new File(getRealPathFromURI(fileUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) return uri.getPath();
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String path = cursor.getString(idx);
        cursor.close();
        return path;
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        updateProfileBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_update_profile);
//
//
//
//    }
}