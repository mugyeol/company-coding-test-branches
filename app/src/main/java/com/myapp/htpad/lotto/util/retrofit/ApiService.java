package com.myapp.htpad.lotto.util.retrofit;

import com.myapp.htpad.lotto.data.LottoModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("common.do?method=getLottoNumber")
    Call<LottoModel> getWinningData(
            @Query("drwNo") int drwNo
    );

}
