package com.myapp.htpad.lotto.view.winningnumber;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.util.retrofit.ApiService;
import com.myapp.htpad.lotto.util.retrofit.RetrofitClientApi;

import java.util.ArrayList;



/**
 * 1등 당첨번호 조회 Activity
 * */


public class WinningNumber extends AppCompatActivity {
    private static final String TAG = "위닝넘버";
    private int[] mResourceSet = {R.id.wnum1,R.id.wnum2,R.id.wnum3,R.id.wnum4,R.id.wnum5,R.id.wnum6,R.id.wnum7};

    @BindView(R.id.winningHeader)TextView header;
    @BindView(R.id.winningExit)TextView exitbtn;
    @BindView(R.id.winningPlus)TextView plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_number);
        ButterKnife.bind(this);
        plus.setVisibility(View.VISIBLE);


        Uri data = getIntent().getData();
        int drwNo = 0;
        if (data!=null) {
            String param = data.getQueryParameter("drwNo");
            if (param != null) {
                try{
                    drwNo = Integer.parseInt(param);
                }catch (NumberFormatException e){
                    //drawNo = 0 으로 pass
                }
                Log.d(TAG,""+drwNo);
            }
        }
        String title = drwNo+"회차 1등 당첨 번호 입니다";
        header.setText(title);
        ApiService apiService = RetrofitClientApi.getClient().create(ApiService.class);

        apiService.getWinningData(drwNo).enqueue(new Callback<LottoModel>() {
            @Override
            public void onResponse(Call<LottoModel> call, Response<LottoModel> response) {
                if (response.body()!=null){
                    if (response.body().getReturnValue().equals(getString(R.string.returnsuccess))){
                        LottoModel lottoModel = response.body();
                        ArrayList<Integer> arrayList = new ArrayList<>();
                        arrayList.add(lottoModel.getDrwtNo1());
                        arrayList.add(lottoModel.getDrwtNo2());
                        arrayList.add(lottoModel.getDrwtNo3());
                        arrayList.add(lottoModel.getDrwtNo4());
                        arrayList.add(lottoModel.getDrwtNo5());
                        arrayList.add(lottoModel.getDrwtNo6());
                        arrayList.add(lottoModel.getBnusNo());

                        for (int i=0; i<arrayList.size(); i++){
                            TextView textView = (TextView)findViewById(mResourceSet[i]);
                            textView.setText(String.valueOf(arrayList.get(i)));
                            textView.setBackground(getDrawable(R.drawable.round));
                        }
                    }else{

                        Log.d(TAG,getString(R.string.returnvaluefail));
                        header.setText(getString(R.string.nodata));
                        plus.setVisibility(View.INVISIBLE);
                    }
                }else {
                    Log.d(TAG,getString(R.string.responsenull));
                    plus.setVisibility(View.INVISIBLE);
                    header.setText(getString(R.string.nodata));
                }
            }

            @Override
            public void onFailure(Call<LottoModel> call, Throwable t) {
                Log.d(TAG,""+t);

                header.setText(getString(R.string.nodata));

            }
        });
        }
        @OnClick(R.id.winningExit)
        void exit(){
        finish();
        }



    }
