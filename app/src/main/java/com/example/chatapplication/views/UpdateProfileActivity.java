package com.example.chatapplication.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.databinding.ActivityUpdateProfileBinding;
import com.example.chatapplication.models.ApiClient;
import com.example.chatapplication.models.UserApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1L);

        if (userId != -1L) {
            String imageUrl = "http://10.0.2.2:8080/api/profile/image?userId=" + userId;

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.default_profile_photo)
                    .error(R.drawable.default_profile_photo)
                    .into(updateProfileBinding.imageViewProfielUpdateProfile);

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .error(android.R.drawable.ic_delete)
                    .into(updateProfileBinding.imageViewProfielUpdateProfile);
        }

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
            Log.d("UPLOAD_DEBUG", "Upload button clicked");
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfileBinding.progressBarUpdateProfile.setVisibility(View.VISIBLE);

            MultipartBody.Part imagePart = prepareFilePart(selectedImageUri, "image");

           // SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
           // long userId = prefs.getLong("userId", -1L);
            RequestBody userIdBody = RequestBody.create(MultipartBody.FORM, String.valueOf(userId));
           // RequestBody userIdBody = RequestBody.create(MultipartBody.FORM, "1"); // Replace "1" with real userId later

            UserApi userApi = ApiClient.getClient().create(UserApi.class);
            Call<ResponseBody> call = userApi.uploadProfileImage(userIdBody, imagePart);

            call.enqueue(new Callback<ResponseBody>() {
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    updateProfileBinding.progressBarUpdateProfile.setVisibility(View.GONE);
                    updateProfileBinding.buttonUpdateProfile.setEnabled(false);
                    if (response.isSuccessful()) {
                        Log.d("UPLOAD", "onResponse: code=" + response.code());
                        Toast.makeText(UpdateProfileActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to MainActivity
                        Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Optional: close UpdateProfileActivity so user can't go back to it with back button

                    } else {
                        Log.d("UPLOAD", "Upload failed: code=" + response.code());
                        Toast.makeText(UpdateProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    updateProfileBinding.buttonUpdateProfile.setEnabled(true);
                    updateProfileBinding.progressBarUpdateProfile.setVisibility(View.GONE);
                    Toast.makeText(UpdateProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

    }
    private File getFileFromUri(Uri uri) {
        File file = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
            file = new File(getCacheDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


    private MultipartBody.Part prepareFilePart(Uri fileUri, String partName) {
        File file = getFileFromUri(fileUri);  // use safe method
        if (file == null) return null;

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


    //TODO: Delete the function on line 198 if not needed at the end *******


























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
//    private void uploadImageToBackend(Uri imageUri) {
//        File file = getFileFromUri(imageUri);
//        if (file == null) {
//            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
//
//        SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
//        Long userIdValue = prefs.getLong("userId", -1L);
//       RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userIdValue));
//
//        UserApi userApi = ApiClient.getClient().create(UserApi.class);
//        Call<ResponseBody> call = userApi.uploadProfileImage(userId, body);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(UpdateProfileActivity.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(UpdateProfileActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(UpdateProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
