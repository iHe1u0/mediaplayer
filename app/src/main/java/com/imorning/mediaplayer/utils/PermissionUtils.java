package com.imorning.mediaplayer.utils;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class PermissionUtils {

    private final static int REQUEST_CODE = 1000;
    private final AppCompatActivity activity;

    public PermissionUtils(AppCompatActivity activity) {
        this.activity = activity;
    }


    /**
     * request for permission
     *
     * @param permission the permission needed.
     */
    public void requestPermission(String permission) {
        if (permission == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (!hasPermission(permission)) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_CODE);
        }

    }


    /**
     * request for permissions
     *
     * @param permissions the permissions
     */
    public void requestPermission(List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return;
        }
        for (String permission : permissions) {
            requestPermission(permission);
        }
    }

    /**
     * check for permission
     *
     * @param permission the permission
     * @return true if has permission
     */
    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }


}
