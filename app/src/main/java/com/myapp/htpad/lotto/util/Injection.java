package com.myapp.htpad.lotto.util;

import android.content.Context;

import com.myapp.htpad.lotto.data.LottoDataBase;
import com.myapp.htpad.lotto.data.LottoLocalDataSource;

public class Injection {

    public static LottoLocalDataSource provideRepositary(Context context){
        LottoDataBase dataBase = LottoDataBase.getInstance(context);
        return LottoLocalDataSource.getInstance(dataBase.lottoDao());
    }
}
