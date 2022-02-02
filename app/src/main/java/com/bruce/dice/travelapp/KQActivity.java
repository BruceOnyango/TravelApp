package com.bruce.dice.travelapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KQActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kq);
    }

    public void book(View view) {
        Uri mypage = Uri.parse("https://www.kenya-airways.com/ke/en/?gclid=Cj0KCQiAi9mPBhCJARIsAHchl1xj5IlBT5Gt5T8uUQKxh306ICUUr9P8s_pU1YmiK64Szes8cYoCdIoaAkxmEALw_wcB");
        Intent intentMpya = new Intent(Intent.ACTION_VIEW,mypage);
        startActivity(intentMpya);
    }
}
