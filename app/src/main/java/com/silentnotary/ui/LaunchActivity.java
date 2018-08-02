package com.silentnotary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.silentnotary.ui.auth.AuthActivity;
import com.silentnotary.util.PrefUtil;

/**
 * Created by albert on 10/16/17.
 */

public class LaunchActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PrefUtil.getSessionId().isEmpty() ){
            startActivity(new Intent(this, AuthActivity.class));

        }else{
            startActivity(new Intent(this, FirstActivity.class));
        }
        finish();
    }
}
