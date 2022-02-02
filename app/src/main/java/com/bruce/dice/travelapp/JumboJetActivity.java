package com.bruce.dice.travelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class JumboJetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jumbojet);
    }

    public void book(View view) {
        Uri mypage = Uri.parse("https://www.jambojet.com/en-glo/");
        Intent intentMpya = new Intent(Intent.ACTION_VIEW,mypage);
        startActivity(intentMpya);
    }
}
