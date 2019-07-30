package com.myapp.htpad.mugyeol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.myapp.htpad.mugyeol.util.MySharedPreference;
import com.myapp.htpad.mugyeol.util.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "로그인액티비티";
    private EditText userid_et;
    private EditText pw_et;
    private boolean isChecboxClicked;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Button login_btn = findViewById(R.id.login_loginbtn);
        userid_et = findViewById(R.id.login_id);
        pw_et = findViewById(R.id.login_password);
        CheckBox checkBox = findViewById(R.id.login_checkbox);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //자동 로그인 확인 위한 변수
                isChecboxClicked = isChecked;
            }
        });


        //저장된 토큰 있으면 자동 로그인.
        if (!MySharedPreference.getInstance(this).getString("token").isEmpty())
            startActivity(new Intent(LoginActivity.this,CarlistActivity.class));
            finish();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userid = userid_et.getText().toString();
                String pw = pw_et.getText().toString();
                String deviceType = "android";

                RetrofitClient.restApi.login(userid,pw,deviceType).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        JsonObject json = (JsonObject) (response.body() != null ? response.body().get("data") : null);
                        String token = json!=null? json.get("token").getAsString():null;

                        if (token!=null){
                            //자동 로그인 체크 여부 && 토큰 저장.
                            if (isChecboxClicked){
                                MySharedPreference.getInstance(LoginActivity.this).putBoolean("boxcheck",isChecboxClicked);
                                MySharedPreference.getInstance(LoginActivity.this).putString("token",token);
                            }
                            startActivity(new Intent(LoginActivity.this,CarlistActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "로그인에 실패 하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(TAG,"login fail "+t);
                        Toast.makeText(LoginActivity.this, "네트워크 상태를 확인해 주세요", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
