package com.myapp.htpad.lotto.view.lottohistory;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.util.Converter;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

class LottoHistoryViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG ="히스토리뷰홀더" ;
    private Activity mActivity;
    @BindView(R.id.itemLottoHistorydrawNo)TextView drawNo;
    @BindView(R.id.itemnum1)TextView num1;
    @BindView(R.id.itemnum2)TextView num2;
    @BindView(R.id.itemnum3)TextView num3;
    @BindView(R.id.itemnum4)TextView num4;
    @BindView(R.id.itemnum5)TextView num5;
    @BindView(R.id.itemnum6)TextView num6;
    @BindView(R.id.itemnum7)TextView num7;

    LottoHistoryViewHolder(Activity activity, ViewGroup parent) {
        super(LayoutInflater.from(activity).inflate(R.layout.item_lottohistory, parent, false));
        ButterKnife.bind(this,itemView); //activity 에서랑 생성자 다름
        this.mActivity = activity;

    }
    void onBind(LottoModel model){
          Log.d(TAG,""+model.getDrwNo());
          String str = model.getDrwNo()+"회차";
            drawNo.setText(str);
            num1.setText(Converter.toString(model.getDrwtNo1()));
            num1.setBackground(mActivity.getDrawable(R.drawable.round));
            num2.setText(Converter.toString(model.getDrwtNo2()));
            num2.setBackground(mActivity.getDrawable(R.drawable.round));
            num3.setText(Converter.toString(model.getDrwtNo3()));
            num3.setBackground(mActivity.getDrawable(R.drawable.round));
            num4.setText(Converter.toString(model.getDrwtNo4()));
            num4.setBackground(mActivity.getDrawable(R.drawable.round));
            num5.setText(Converter.toString(model.getDrwtNo5()));
            num5.setBackground(mActivity.getDrawable(R.drawable.round));
            num6.setText(Converter.toString(model.getDrwtNo6()));
            num6.setBackground(mActivity.getDrawable(R.drawable.round));
            num7.setText(Converter.toString(model.getBnusNo()));
            num7.setBackground(mActivity.getDrawable(R.drawable.round));


    }
}