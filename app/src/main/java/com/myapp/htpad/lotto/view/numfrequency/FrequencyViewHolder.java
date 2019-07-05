package com.myapp.htpad.lotto.view.numfrequency;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.FrequencyModel;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

 class FrequencyViewHolder extends RecyclerView.ViewHolder {

    private Activity mActivity;

    @BindView(R.id.numfrequencyNum)TextView numTextView;
    @BindView(R.id.numFrequencyCounting)TextView countingTextView;
    @BindView(R.id.numfrequencyView) View view;

     FrequencyViewHolder(Activity activity, ViewGroup parent) {
        super(LayoutInflater.from(activity).inflate(R.layout.item_numfrequency, parent, false));
        mActivity= activity;
        ButterKnife.bind(this,itemView);



    }

    void onBind(FrequencyModel frequencyModel){
        numTextView.setText(String.valueOf(frequencyModel.getNum()));
        numTextView.setBackground(mActivity.getDrawable(R.drawable.round));
        String text = frequencyModel.getSum()+"회";
        countingTextView.setText(text);

    }
}
