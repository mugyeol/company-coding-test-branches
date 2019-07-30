package com.myapp.htpad.mugyeol;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
import com.myapp.htpad.mugyeol.util.MySharedPreference;
import com.myapp.htpad.mugyeol.util.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarlistActivity extends AppCompatActivity {

    private static final String TAG = "카리스트";
    private Adapter mAdapter;
    private ImageView search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlist);

        RecyclerView recyclerView = findViewById(R.id.carlist_recyclerview);

        //TODO search btn for test. 나중에 지울 것
        search = findViewById(R.id.carlist_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreference.getInstance(CarlistActivity.this).removeToken();
            }
        });


        mAdapter = new Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);


        RetrofitClient.restApi.getCarlist("Bearer "+
                MySharedPreference.getInstance(CarlistActivity.this).getString("token")).enqueue(new Callback<CarModel>() {
            @Override
            public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                CarModel carModel= response.body();
                List<CarModel.Data> cars = carModel != null ? carModel.getData() : null;
               if (cars!=null && cars.size()>0)
                   Collections.sort(cars);
                   mAdapter.addAll(cars);

            }

            @Override
            public void onFailure(Call<CarModel> call, Throwable t) {
                Toast.makeText(CarlistActivity.this, "fail", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"fail : "+t);
            }
        });

    }


    @Override
    protected void onDestroy() {
        //자동 로그인 체크 하지 않은 경우 shared 정보 clear
        if (!MySharedPreference.getInstance(this).getBoolean("boxcheck",false)){
            MySharedPreference.getInstance(this).removeToken();
        }
        super.onDestroy();

    }

    class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CarModel.Data> arrayList = new ArrayList<>();
    private Context mContext;
    Adapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car,viewGroup,false);


        return new MyViewHolder(itemview);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private ToggleButton toggleButton;
        private TextView description;
        private TextView carNumber;
        private TextView loadQty;
        private LinearLayout linearLayout;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            toggleButton = itemView.findViewById(R.id.caritem_toggle);
            description = itemView.findViewById(R.id.caritem_description);
            carNumber = itemView.findViewById(R.id.caritem_carid);
            loadQty = itemView.findViewById(R.id.caritem_loadqty);
            linearLayout = itemView.findViewById(R.id.caritem_linear);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int pos) {
        MyViewHolder viewHolder = ((MyViewHolder)holder);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) viewHolder.linearLayout.getLayoutParams();
        lp.topMargin = 15;
        if (pos==0){
            viewHolder.linearLayout.setLayoutParams(lp);
        }

       viewHolder.description.setText(arrayList.get(pos).getDescription());
       viewHolder.carNumber.setText(arrayList.get(pos).getLicenseNumber());
       if (arrayList.get(pos).getFavorite()){
           viewHolder.toggleButton.setChecked(true);
       }else{
           viewHolder.toggleButton.setChecked(false);

       }
       viewHolder.loadQty.setText(qty_str(arrayList.get(pos).getCapacity()));



       viewHolder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   Log.d(TAG,MySharedPreference.getInstance(mContext).getString("token"));
                   Log.d(TAG,arrayList.get(pos).getVehicleIdx()+"");

                   RetrofitClient.restApi.putFavorite("Bearer "+MySharedPreference.getInstance(mContext).getString("token")
                           ,arrayList.get(pos).getVehicleIdx(), true)
                           .enqueue(new Callback<JsonObject>() {
                       @Override
                       public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                           Log.d(TAG,""+ (response.body()));
                       }

                       @Override
                       public void onFailure(Call<JsonObject> call, Throwable t) {
                           Log.d(TAG,""+ t);

                       }
                   });

               }else{
                        //favorite 해제
               }
           }
       });




    }
    private String qty_str (int capacity){
        double qty_ton = capacity/1000.0;

        if (capacity%1000==0){
            return (int)qty_ton+"t";
        }

        return qty_ton+"t";
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void addAll(List<CarModel.Data> cars){
        this.arrayList = cars;
        notifyDataSetChanged();
    }
}

}

