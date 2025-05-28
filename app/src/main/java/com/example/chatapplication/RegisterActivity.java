package com.example.chatapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.chatapplication.databinding.ActivityRegisterBinding;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    ArrayList<String> permissionsList = new ArrayList<>();
    ActivityRegisterBinding registerBinding;
    ActivityResultLauncher<String[]>permissionResultLauncher;
    int denioedPermissionsCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());
        if (Build.VERSION.SDK_INT > 33) { // We use 32 version
//            permissionsList.add(Manifest.permission) //TO BE CONTINUED FROM HERE
            
        }


        registerActivityFromMultiplePermissions();
        registerBinding.imageViewProfielRegister.setOnClickListener(v ->{



        });

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

    public void openPhotoPicker(){

    }

    public void shouldShowPremissionRationalIfNeeded(){
        ArrayList<String>deniedPermissionsCollector = new ArrayList<>();

    }
}