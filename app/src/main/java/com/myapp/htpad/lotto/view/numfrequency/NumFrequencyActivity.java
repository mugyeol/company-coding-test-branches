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
            public void onCountedComplete() {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG,""+arrayList.size());
                    Collections.sort(arrayList);

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



    private void getNumsCalculated (Callback callback){
        for (int i=1; i<46; i++){
            Injection.provideRepositary(this).countNums(i, new LottoDataSource.GetNumCallback() {
                @Override
                public void onCounted(FrequencyModel frequencyModel) {
                    arrayList.add(frequencyModel);
                        Log.d(TAG,"num "+frequencyModel.getNum()+"sum : "+frequencyModel.getSum());
                    if (arrayList.size()==45){
                        callback.onCountedComplete();
                    }
                }
            });
            }
    }

    private interface Callback{
        void onCountedComplete();
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
