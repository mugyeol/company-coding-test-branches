package com.myapp.htpad.lotto.view.welcome;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.view.main.MainActivity;


/**
 * welcome activity : 처음 앱 실행 시에만 open
 * */


public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.welcomeActivityOk)
    void goMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
