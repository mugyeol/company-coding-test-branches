package com.myapp.htpad.lotto.data;

import java.util.List;

public interface LottoDataSource {

    interface LoadDataCallback{
        void onDataLoaded(List<LottoModel> lottoModels);
        void onFail();
    }

    void getLottos(LoadDataCallback callback);
    void insertLotto(LottoModel lottoModel);




}
