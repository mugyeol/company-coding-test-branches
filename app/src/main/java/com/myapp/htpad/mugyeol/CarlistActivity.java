package com.myapp.htpad.mugyeol;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import retrofit2.Response;

public class CarlistActivity extends AppCompatActivity {

    private static final String TAG = "카리스트";
    private Adapter mAdapter;
    private EditText searchEdit;


    private List<CarModel.Data> cars = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carlist);

        searchEdit = findViewById(R.id.carlist_search);
        RecyclerView recyclerView = findViewById(R.id.carlist_recyclerview);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.carlist_swipe);

        mAdapter = new Adapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        //검색 기능
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchEdit.getText().toString();
                search(text);

            }
        });


        //최초 차량 정보 불러오기
        getCarinfo(new Callback() {
            @Override
            public void onSuccess() {
                // nothing
            }

            @Override
            public void onFail() {
                // nothing
            }
        });

        //새로고침
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCarinfo(new Callback() {
                    @Override
                    public void onSuccess() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFail() {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                });

            }
        });


        //TODO search btn for test. 나중에 지울 것
        ImageView search = findViewById(R.id.carlist_searchicon);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySharedPreference.getInstance(CarlistActivity.this).removeToken();
            }
        });
    }

        private void getCarinfo(final Callback callback){
            RetrofitClient.restApi.getCarlist("Bearer "+
                    MySharedPreference.getInstance(CarlistActivity.this).getString("token")).enqueue(new retrofit2.Callback<CarModel>() {
                @Override
                public void onResponse(Call<CarModel> call, Response<CarModel> response) {
                    CarModel carModel= response.body();
                    cars = carModel != null ? carModel.getData() : null;

                    if (cars!=null && cars.size()>0){
                        //최초 데이터 불러올 경우 & 검색어 없을때 새로고침 하는 경우
                        if (searchEdit.getText().toString().isEmpty()){
                            //favorite 기준으로 정렬
                            Collections.sort(cars);
                            mAdapter.addAll(cars);

                        }
                        //검색어 있는 경우 새로고침
                        else{
                            search(searchEdit.getText().toString());
                        }
                        callback.onSuccess();
                    }
                }

                @Override
                public void onFailure(Call<CarModel> call, Throwable t) {
                    Toast.makeText(CarlistActivity.this, "데이터를 불러오는데 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    callback.onFail();
                    Log.d(TAG,"fail : "+t);
                }
            });
        }
        private void search(String text){

        List<CarModel.Data> searchedList = new ArrayList<>();


        //검색어에 부합하는 객체만 searchedList에 추가
                for (int i=0; i<cars.size(); i++){
                    if (cars.get(i).getDescription().contains(text)
                            || cars.get(i).getLicenseNumber().contains(text)
                            || qty_str(cars.get(i).getCapacity()).contains(text)){

                        searchedList.add(cars.get(i));
                    }
                }

                Collections.sort(searchedList);
                mAdapter.addAll(searchedList);
        }
    private interface  Callback {

        void onSuccess();
        void onFail();

    }
    private String qty_str (int capacity){
        double qty_ton = capacity/1000.0;

        //소수점 아래 없는 경우 int형으로 형변환
        if (capacity%1000==0){
            return (int)qty_ton+"t";
        }

        return qty_ton+"t";
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
        private ToggleButton favorite_icon;
        private TextView description;
        private TextView carNumber;
        private TextView loadQty;
        private ConstraintLayout constraint;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            favorite_icon = itemView.findViewById(R.id.caritem_favoriteicon);
            description = itemView.findViewById(R.id.caritem_description);
            carNumber = itemView.findViewById(R.id.caritem_carid);
            loadQty = itemView.findViewById(R.id.caritem_loadqty);
            constraint = itemView.findViewById(R.id.caritem_constraint);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        final MyViewHolder viewHolder = ((MyViewHolder)holder);
       viewHolder.description.setText(arrayList.get(pos).getDescription());
       viewHolder.carNumber.setText(arrayList.get(pos).getLicenseNumber());
       //단위 L-> t으로 변환하여 setText
       viewHolder.loadQty.setText(qty_str(arrayList.get(pos).getCapacity()));
       viewHolder.favorite_icon.setChecked(arrayList.get(pos).getFavorite());


       // SET MARGINTOP FOR 첫번째 카드뷰
        if (pos==0){
            int firstmargintop = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    14,
                    mContext.getResources().getDisplayMetrics()
            );
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)viewHolder.constraint.getLayoutParams();
            marginLayoutParams.topMargin = firstmargintop;
            viewHolder.constraint.setLayoutParams(marginLayoutParams);
        }


       viewHolder.favorite_icon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
               //view에서 바뀔때만 update
            if (buttonView.isPressed()){
                updateFavorite(isChecked, viewHolder.getAdapterPosition(), new CarlistActivity.Callback() {
                    @Override
                    public void onSuccess() {
                        if (isChecked){
                            Toast.makeText(mContext, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "즐겨찾기를 해제했습니다", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onFail() {
                        if (isChecked){
                            Toast.makeText(mContext, "즐겨찾기 추가에 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "즐겨찾기 해제에 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();

                        }
                        viewHolder.favorite_icon.setChecked(isChecked);

                    }
                });
            }

           }
       });




    }
    private void updateFavorite(final boolean favorite, final int pos, final CarlistActivity.Callback callback){

        RetrofitClient.restApi.putFavorite("Bearer "+MySharedPreference.getInstance(mContext).getString("token")
                ,arrayList.get(pos).getVehicleIdx(), favorite)
                .enqueue(new retrofit2.Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            JsonObject jsonObject = response.body();
                            String result = jsonObject != null ? jsonObject.get("data").getAsString() : null;
                            if (result!=null){
                                if (result.equals("ok")){
                                    //스크롤 할때 즐겨찾기 업데이트 된거 유지되도록
                                    arrayList.get(pos).setFavorite(favorite);
                                    callback.onSuccess();
                                }
                            }

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(TAG,""+ t);
                        callback.onFail();

                    }
                });

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

