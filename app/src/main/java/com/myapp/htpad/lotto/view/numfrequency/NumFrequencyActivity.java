package com.myapp.htpad.lotto.view.numfrequency;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.myapp.htpad.lotto.R;
import com.myapp.htpad.lotto.data.FrequencyModel;
import com.myapp.htpad.lotto.data.LottoDataSource;
import com.myapp.htpad.lotto.data.LottoModel;
import com.myapp.htpad.lotto.util.Injection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 *
 * 빈출 번호 자주 출현한 번호부터 목록 보여주는 Activity
 *
 *
 *
 * */


public class NumFrequencyActivity extends AppCompatActivity {
    private static final String TAG = "프리퀀시";

    @BindView(R.id.NumFrequencyRecyclerView)RecyclerView recyclerView;
    private FrequencyAdapter mAdapter;
    private ArrayList<FrequencyModel> arrayList= new ArrayList<>();
    @BindView(R.id.numFrequencyPbar) ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_frequency);

        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar.setVisibility(View.VISIBLE);


        getNumsCalculated(new Callback() {

            @Override
            public void onCountedComplete(ArrayList<FrequencyModel> arrayList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    mAdapter = new FrequencyAdapter(NumFrequencyActivity.this,arrayList);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                }
            });


            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG+"LIFECYCLE","ONDESTORY");
        arrayList.clear();
    }


 // 번호 별로 출현 횟수 frequency model에 담아서 (번호, 출현 횟수) array에 저장
// array에 저장 완료되면 oncountedcomplete 호출 -> UI UPDATE
    private void getNumsCalculated (Callback callback){

        for (int i=1; i<46; i++){
            Injection.provideRepositary(this).countNums(i, new LottoDataSource.GetNumCallback() {
                @Override
                public void onCounted(FrequencyModel frequencyModel) {
                    arrayList.add(frequencyModel);
                        Log.d(TAG,"num "+frequencyModel.getNum()+"sum : "+frequencyModel.getSum());
                    if (arrayList.size()==45){
                        Collections.sort(arrayList);
                        callback.onCountedComplete(arrayList);
                    }
                }
            });
            }
    }

    private interface Callback{
        void onCountedComplete(ArrayList<FrequencyModel> arrayList);
    }

    public class FrequencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<FrequencyModel> mArraylist;
        private Activity mActivity;
        FrequencyAdapter (Activity activity, ArrayList<FrequencyModel> arrayList){
            mActivity = activity;
            mArraylist = arrayList;

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new FrequencyViewHolder(mActivity,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG,"BINDVIEW");
            FrequencyViewHolder viewHolder = (FrequencyViewHolder)holder;
            viewHolder.onBind(mArraylist.get(position));
        }

        @Override
        public int getItemCount() {
            return mArraylist.size();
        }



    }
}
