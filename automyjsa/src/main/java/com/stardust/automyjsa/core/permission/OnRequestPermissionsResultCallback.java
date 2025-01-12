package com.stardust.automyjsa.core.permission;

import androidx.annotation.NonNull;

public interface OnRequestPermissionsResultCallback {

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
