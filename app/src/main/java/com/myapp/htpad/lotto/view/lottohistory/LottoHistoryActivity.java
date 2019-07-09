package com.myapp.htpad.lotto.view.lottohistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.LottoDataSource;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.util.Injection;

import java.util.ArrayList;
import java.util.List;


/**
 * 1-50회차 당첨 번호 조회 Activity
 * */


public class LottoHistoryActivity extends AppCompatActivity {


    @BindView(R.id.lottohistoryRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.lottohistoryTextview)TextView title;

    private Adapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottohistory);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Adapter(this);
        recyclerView.setAdapter(mAdapter);


        Injection.provideRepositary(this).getLottos(new LottoDataSource.LoadDataCallback() {
            @Override
            public void onDataLoaded(List<LottoModel> lottoModels) {
                if (!lottoModels.isEmpty()){
                    mAdapter.addAll(lottoModels);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFail() {
                showMissingData();
            }
        });




    }
    private void showMissingData(){
        title.setText(R.string.nodata);
    }


    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<LottoModel> mArraylist = new ArrayList<>();
        private Activity mActivity;
        Adapter (Activity activity){
            mActivity = activity;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new LottoHistoryViewHolder(mActivity,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            LottoHistoryViewHolder viewHolder = (LottoHistoryViewHolder)holder;
            viewHolder.onBind(mArraylist.get(position));
        }

        @Override
        public int getItemCount() {
            return mArraylist.size();
        }

        void addAll(List<LottoModel> lottoModels){
            this.mArraylist = lottoModels;
        }


    }

}
