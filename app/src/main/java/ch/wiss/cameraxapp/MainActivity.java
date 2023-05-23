package ch.wiss.cameraxapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;


/**
 * simple camerax based camera app, based on
 * https://medium.com/swlh/introduction-to-androids-camerax-with-java-ca384c522c5
 * and largely on ChatGPT for saving the pictures
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final String[] CAMERA_PERMISSION = new String[]{android.Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    ListView liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enableCamera = findViewById(R.id.btnEnableCamera);
        enableCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    enableCamera();
                } else {
                    requestPermission();
                }
            }
        });

        ImageView imageView = findViewById(R.id.imageView);

        liste = findViewById(R.id.liste);
        // loads selected image file in preview
        liste.setOnItemClickListener((v,adapter, index,id)->{
            String file = (String)v.getItemAtPosition(index);
            Log.d("Sven","selected item: "+ file);
            imageView.setImageURI(Uri.fromFile(new File(getExternalMediaDirs()[0], file)));

                });

        readFiles();
    }

    protected void onResume(){
        super.onResume();
        readFiles();
    }

    /**
     * handles display of files in listView
     */
    private void readFiles(){
        String[] files = getExternalMediaDirs()[0].list();
        for (String f : files){
            Log.d("Sven-Files", f);
        }
        ArrayAdapter<String> myPictures = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                files );
        liste.setAdapter(myPictures);
    }

    private void enableCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }
}