package ru.mirea.zhumanov.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import ru.mirea.zhumanov.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        binding.editTextGroup.setText(sharedPref.getString("GROUP", "your group"));
        binding.editTextFilm.setText(sharedPref.getString("Film", "your film"));
        binding.editTextNumber.setText(sharedPref.getString("Number", "your number"));
    }

    public void buttonOnClick(View view){
        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("GROUP", String.valueOf(binding.editTextGroup.getText()));
        editor.putString("Film", String.valueOf(binding.editTextFilm.getText()));
        editor.putString("Number", String.valueOf(binding.editTextNumber.getText()));



        editor.apply();


    }
}