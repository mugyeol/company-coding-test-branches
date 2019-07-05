package com.myapp.htpad.lotto.data;

import java.util.List;

public interface LottoDataSource {

    interface LoadDataCallback{
        void onDataLoaded(List<LottoModel> lottoModels);
        void onFail();
    }
    interface GetNumCallback{
        void onCounted(FrequencyModel frequencyModel);
    }

    void getLottos(LoadDataCallback callback);
    void insertLotto(LottoModel lottoModel);
    void countNums (int i, GetNumCallback callback);



}
