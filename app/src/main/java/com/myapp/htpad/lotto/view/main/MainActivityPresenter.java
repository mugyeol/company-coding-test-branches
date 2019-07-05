package com.myapp.htpad.lotto.view.main;

import android.util.Log;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.LottoLocalDataSource;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.view.splash.SplashActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private static final String TAG = "메인프리젠터";
    private ArrayList<Integer> mNumSet;
    private MainActivityContract.View mView;

    @Override
    public void attachView(MainActivityContract.View view) {
            mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    @Override
    public void createNum(String num) {

        mNumSet = new ArrayList<>();
        if (num.equals("")){
            mView.showDialog(mView.getContext().getString(R.string.putmoreinput),"");
        }else{
            for (int i=0; i<7; i++){
                int ran = (int)(Math.random()*45)+1;
                Log.d(TAG,"ran : "+ran);
                Log.d(TAG,"i : "+i);
                if (i==0){
                    mNumSet.add(ran);
                }else{
                    if (mNumSet.contains(ran)){
                        i--;
                    }else{
                        mNumSet.add(ran);
                    }
                }
            }
            //보너스 빼고 오름차순 정렬
            int bonus = mNumSet.get(6);
            mNumSet.remove(6);
            Collections.sort(mNumSet);
            mNumSet.add(bonus);
            Log.d(TAG,mNumSet.toString());

            mView.onCreatedNum(mNumSet, num);

        }




    }

    @Override
    public void checkIfWin(int num, ArrayList<Integer> getneratedNums) {
        SplashActivity.mApiService.getWinningData(num).enqueue(new Callback<LottoModel>() {
            @Override
            public void onResponse(Call<LottoModel> call, Response<LottoModel> response) {
                if (response.body()!=null){
                    if (response.body().getReturnValue().equals(mView.getContext().getString(R.string.returnsuccess))){
                        LottoModel lottoModel = response.body();
                        int num1 = lottoModel.getDrwtNo1();
                        int num2 = lottoModel.getDrwtNo2();
                        int num3 = lottoModel.getDrwtNo3();
                        int num4 = lottoModel.getDrwtNo4();
                        int num5 = lottoModel.getDrwtNo5();
                        int num6 = lottoModel.getDrwtNo6();
                        int bnus = lottoModel.getBnusNo();
                        String dot = ", ";
                        String winNum = num1+dot+num2+dot+num3+dot+num4+dot+num5+dot+num6+" + "+bnus+"(보너스) \r\n입니다";
                        //TODO 1-5등 예외처리
                        if (getneratedNums.get(0)==num1 && getneratedNums.get(1)==num2
                                && getneratedNums.get(2)==num3 && getneratedNums.get(3)==num4
                                && getneratedNums.get(4)==num5 && getneratedNums.get(5)==num6
                                && getneratedNums.get(6)==bnus
                                ){
                            mView.showDialog("1등에 당첨 되었습니다.","당첨번호는 "+"\r\n"+winNum);
                        }else{
                            mView.showDialog("1등에 당첨 되지 않았습니다","당첨번호는 "+"\r\n"+winNum);
                        }





                    }else{
                        Log.d(TAG,"return value fail");
                        mView.showDialog("아직 진행하지 않은 회차 혹은 존재하지 않는 회차 입니다","");
                    }
                }else {
                    Log.d(TAG,"response null");
                    mView.showDialog("네트워크 에러입니다. 다시 시도해 주세요","");

                }

            }

            @Override
            public void onFailure(Call<LottoModel> call, Throwable t) {

            }
        });
    }
}
