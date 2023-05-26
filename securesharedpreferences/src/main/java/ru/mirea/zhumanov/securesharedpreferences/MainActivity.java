package ru.mirea.zhumanov.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.zhumanov.securesharedpreferences.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    SharedPreferences sharedPref;

    SharedPreferences secureSharedPreferences;

    PreferenceManager preferenceManager;

    private String fileName = "mayakovski.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager=PreferenceManager.getInstance(this);

        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);


        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = null;
        try {
            mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        binding.editTextTextPersonName.setText(secureSharedPreferences.getString("Lyricist1", "unknown"));

        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File (sdCard.getAbsolutePath());
        File file = new File(directory, "mayakovski.jpg"); //or any other format supported

        file.getAbsolutePath();

    }

    public void rememberButton(View view) throws GeneralSecurityException, IOException {
        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

        secureSharedPreferences = EncryptedSharedPreferences.create(
                "secret_shared_prefs",
                mainKeyAlias,
                getBaseContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );

        secureSharedPreferences.edit().putString("secure", "ЛЮБИМЫЙ ПОЭТ").apply();

        secureSharedPreferences.edit().putString("Lyricist1", String.valueOf(binding.editTextTextPersonName.getText())).apply();

    }

    public void loadButton(View view) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.mayakovski);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        preferenceManager.setString("image_data",encodedImage);

        preferenceManager=PreferenceManager.getInstance(this);

        String previouslyEncodedImage = preferenceManager.getString("image_data");

        if( !previouslyEncodedImage.equalsIgnoreCase("") ){
            b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            binding.imageView.setImageBitmap(bitmap);
        }
    }
}