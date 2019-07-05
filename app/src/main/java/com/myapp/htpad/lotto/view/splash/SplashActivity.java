package com.myapp.htpad.lotto.view.splash;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.LottoLocalDataSource;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.util.Injection;
import com.myapp.htpad.lotto.util.retrofit.ApiService;
import com.myapp.htpad.lotto.util.retrofit.RetrofitClientApi;
import com.myapp.htpad.lotto.view.main.MainActivity;
import com.myapp.htpad.lotto.view.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = "스플래쉬";
    public static ApiService mApiService;
    private static final int delayForSplash = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("firstComing",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.d(TAG," : "+sharedPreferences.getBoolean(getString(R.string.keyFirstCome),false));

        LottoLocalDataSource lottoLocalDataSource = Injection.provideRepositary(this);


        mApiService = RetrofitClientApi.getClient().create(ApiService.class);

        //앱을 처음 실행 시킬 때에만 로또 정보 저장.
        if (!sharedPreferences.getBoolean(getString(R.string.keyFirstCome),false)){

            for (int i=1; i<51; i++){
                mApiService.getWinningData(i).enqueue(new Callback<LottoModel>() {
                    @Override
                    public void onResponse(Call<LottoModel> call, Response<LottoModel> response) {
                        if (response.body()!=null){
                            if (response.body().getReturnValue().equals(getString(R.string.returnsuccess))){
                                LottoModel lottoModel = response.body();
                                lottoLocalDataSource.insertLotto(lottoModel);
                            }else{
                                Log.d(TAG,getString(R.string.returnvaluefail));
                            }
                        }else {
                            Log.d(TAG,getString(R.string.responsenull));
                        }


                    }

                    @Override
                    public void onFailure(Call<LottoModel> call, Throwable t) {

                    }
                });
            }

            editor.putBoolean(getString(R.string.keyFirstCome),true);
            editor.apply();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
                }
            },delayForSplash);


        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
            },delayForSplash);        }





    }
}
