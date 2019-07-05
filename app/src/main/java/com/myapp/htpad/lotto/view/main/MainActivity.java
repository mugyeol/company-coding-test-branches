package com.myapp.htpad.lotto.view.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.view.lottohistory.LottoHistoryActivity;
import com.myapp.htpad.lotto.view.numfrequency.NumFrequencyActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    private static final String TAG = "메인액티비티" ;
    private int[] mResourceSet = {R.id.num1,R.id.num2,R.id.num3,R.id.num4,R.id.num5,R.id.num6,R.id.num7};
    private MainActivityContract.Presenter mPresenter;
    private int recentSearchNum;
    private ArrayList<Integer> mNumset= new ArrayList<>();

    @BindView(R.id.MainActivityLinearNum) LinearLayout mLinearNum;
    @BindView(R.id.MainActivityCreateBtn) Button mCreateButton;
    @BindView(R.id.MainActivityHintForNum) TextView mNumHint;
    @BindView(R.id.MainActivityEditText) EditText mEdittext;
    @BindView(R.id.MainActivityCheckBbtn) Button mCheckButton;
    @BindView(R.id.MainActivityCheckHistory)Button mHistoryButton;
    @BindView(R.id.MainActivityNumFrequency)Button mFrequencyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mPresenter = new MainActivityPresenter();
        mPresenter.attachView(this);
        mLinearNum.setVisibility(View.INVISIBLE);
        mCheckButton.setVisibility(View.INVISIBLE);


        mFrequencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrequencyBtn.setClickable(false);
                startActivity(new Intent(MainActivity.this,NumFrequencyActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG+"LIFECYCLE","ON RESUME");
        mFrequencyBtn.setClickable(true);
    }

    @OnClick(R.id.MainActivityCreateBtn)
    void createNum(){
        String num = mEdittext.getText().toString();
        mPresenter.createNum(num);
        mEdittext.setText("");
    }
    @OnClick(R.id.MainActivityCheckHistory)
    void checkHistory(){
        startActivity(new Intent(MainActivity.this,LottoHistoryActivity.class));
    }

    @OnClick(R.id.MainActivityCheckBbtn)
    void checkWinning(){
        mPresenter.checkIfWin(recentSearchNum,mNumset);
    }



    @Override
    public void onCreatedNum(ArrayList<Integer> arrayList, String num) {
        for (int i=0; i<arrayList.size(); i++){
             TextView textView =  (TextView) findViewById(mResourceSet[i]);
             textView.setText(String.valueOf(arrayList.get(i)));
             textView.setBackground(getDrawable(R.drawable.round));
        }

        //당첨인지 확인 할 때 사용할 param
        mNumset = arrayList;
        try {
            recentSearchNum = Integer.parseInt(num);

        }catch (NumberFormatException e){

            recentSearchNum=0;
        }


        String numStr = num+getString(R.string.checkNlotto);
        mCheckButton.setText(numStr);

        mLinearNum.setVisibility(View.VISIBLE);
        mCheckButton.setVisibility(View.VISIBLE);



    }

    @Override
    public void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.show();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onCheckedIfWin(boolean isWon) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
