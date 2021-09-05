package com.imorning.mediaplayer.activity;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.imorning.mediaplayer.utils.PermissionUtils;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PermissionUtils(this).requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
