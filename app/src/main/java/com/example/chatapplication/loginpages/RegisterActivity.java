package com.example.chatapplication.loginpages;
import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.chatapplication.models.ApiClient;
import com.example.chatapplication.RegisterRequest;
import com.example.chatapplication.models.UserApi;
import com.example.chatapplication.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ArrayList<String> permissionsList = new ArrayList<>();
    ActivityRegisterBinding registerBinding;
    ActivityResultLauncher<String[]>permissionResultLauncher;
    int denioedPermissionsCount = 0;

    ActivityResultLauncher<Intent> photoPickerResultLancher;
    ActivityResultLauncher<Intent> cropPhotoResultLauncher;

    Uri croppedImageUri;
    private String userName;
    private String userEmail;
    private String userPassword;
    boolean imageControl = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());
        if (Build.VERSION.SDK_INT > 33) { // We use 32 version
            permissionsList.add(Manifest.permission.READ_MEDIA_IMAGES);
            permissionsList.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED);
        } else if (Build.VERSION.SDK_INT > 32){
            permissionsList.add(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        registerActivityFromMultiplePermissions();
        registerActivityForPhotoPicker();
        registerAcitivityForPhotoCrop();

        registerBinding.imageViewProfielRegister.setOnClickListener(v ->{

           if(hasPermission()){
               openPhotoPicker();
           }else {
               shouldShowPremissionRationalIfNeeded();
           }

        });

        registerBinding.buttonSignup.setOnClickListener(v -> {
            createNewUser();  //
        });


    }
    public void uploadPhoto(){

    }

    public void registerActivityFromMultiplePermissions(){
        permissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result->{
            boolean allGranted = true;

            for (Boolean isAllowed: result.values()){
                if (!isAllowed){
                    allGranted = false;
                    break;
                }
            }
            if (allGranted){
                openPhotoPicker();
            }
            else {
                denioedPermissionsCount++;
                if (denioedPermissionsCount < 2) {
                    shouldShowPremissionRationalIfNeeded();
                }

                else{
                    //When this dialog appears, the user will be able to grant permission
                    //from the application's settings
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                    builder.setTitle("Chat App");
                    builder.setMessage("You can grant the necessary permission to access the photos from the application settings.");
                    builder.setPositiveButton("Go App Settings", (dialog,which) ->{

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.parse("package:" + getPackageName());
                        intent.setData(uri);
                        startActivity(intent);
                        dialog.dismiss();

                    });
                    builder.setNegativeButton("Dismiss",(dialog,which) ->{
                        dialog.dismiss();
                    });
                    builder.create().show();
                }
            }
        });

    }

    public void createNewUser() {
        String userName = registerBinding.editTextUserNameRegister.getText().toString().trim();
        String userEmail = registerBinding.editTextEmailSignup.getText().toString().trim();
        String userPassword = registerBinding.editTextPasswordSignup.getText().toString().trim();

        if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Please fill correctly the name, email or the password", Toast.LENGTH_SHORT).show();
        } else {
            registerBinding.buttonSignup.setEnabled(false);
            registerBinding.progressBarSignup.setVisibility(View.VISIBLE);

            RegisterRequest request = new RegisterRequest(userName, userEmail, userPassword);
            UserApi userApi = ApiClient.getClient().create(UserApi.class);

            userApi.registerUser(request).enqueue(new Callback<ResponseBody>() {
                @Override

                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    registerBinding.progressBarSignup.setVisibility(View.GONE);
                    registerBinding.buttonSignup.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(RegisterActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish(); // so user can't go back to register with back button


                    } else {
                        Toast.makeText(RegisterActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    registerBinding.progressBarSignup.setVisibility(View.GONE);
                    registerBinding.buttonSignup.setEnabled(true);
                    Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void openPhotoPicker(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerResultLancher.launch(intent);
    }

    public void registerActivityForPhotoPicker(){
      photoPickerResultLancher = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), result -> {

          int resultCode = result.getResultCode();
          Intent data = result.getData();

          if(resultCode == RESULT_OK && data != null){

              Uri uncroppedImageUri = data.getData();
          }

      });
    }

    public void registerAcitivityForPhotoCrop(){

        cropPhotoResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), cropResult ->{

                 int resultCode = cropResult.getResultCode();
                 Intent data = cropResult.getData();

                 if(resultCode == RESULT_OK && data != null) {
                    croppedImageUri = UCrop.getOutput(data);
                    if(croppedImageUri != null){
                        Picasso.get().load(croppedImageUri).into(registerBinding.imageViewProfielRegister);
                        imageControl = true;
                    }
                 } else if (resultCode == UCrop.RESULT_ERROR && data != null) {

                     Toast.makeText(this, UCrop.getError(data).getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                 }


        });

    }

    public void cropSelectedImage(Uri sourceUri){

        //we call getCacheDir() to get cache dir method returns a directory that we can use to store application's temp files,
        // these files can automatically get deleted when the application closed or when there is not place or memory.
        //if user clicks on the image view again after the cropping process and then repeats the cropping procces the
        // first cropp photo will continueto appear so we add the time in mili seconds
       Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped" + System.currentTimeMillis()));
       Intent croppedIntent = UCrop.of(sourceUri,destinationUri)
               .withAspectRatio(1,1)
               .getIntent(RegisterActivity.this);
       cropPhotoResultLauncher.launch(croppedIntent);

    }



    public void shouldShowPremissionRationalIfNeeded(){
        ArrayList<String>deniedPermissions = new ArrayList<>();

        for (String permission : permissionsList){
          if(ActivityCompat.shouldShowRequestPermissionRationale(this,permission)) {
             deniedPermissions.add(permission);
            }
        }

        if(!deniedPermissions.isEmpty()) {

            Snackbar.make(registerBinding.mainRegister, "Please grant necessary permissions to add a profile picture", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", v -> {
                        permissionResultLauncher.launch(deniedPermissions.toArray(new String[0]));
                    }).show();

        } else {
            permissionResultLauncher.launch(permissionsList.toArray(new String[0]));
        }

    }
    public boolean hasPermission(){
        for(String permission : permissionsList){

            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}