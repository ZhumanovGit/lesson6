package ru.mirea.zhumanov.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.zhumanov.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    String fileName = "MyHistory.txt";

    ActivityMainBinding binding;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String text = "My history";
        tv = binding.dataText;
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tv.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(getTextFromFile());
                    }
                });
            }
        }).start();
    }

    private String getTextFromFile() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(fileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            String text = new String(bytes);
            Log.d(TAG, text);
            return text;
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return null;
    }


}