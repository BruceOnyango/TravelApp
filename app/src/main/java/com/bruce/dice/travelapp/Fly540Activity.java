package com.bruce.dice.travelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Fly540Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly540);
    }

    public void book(View view) {
        Uri mypage = Uri.parse("https://www.fly540.com/");
        Intent intentMpya = new Intent(Intent.ACTION_VIEW,mypage);
        startActivity(intentMpya);
    }
}
