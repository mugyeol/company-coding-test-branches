package com.myapp.htpad.lotto.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {LottoModel.class}, version = 1)
public abstract class LottoDataBase extends RoomDatabase {

    private static LottoDataBase INSTANCE;

    public abstract LottoDao lottoDao();

    private static final Object sLock = new Object();

    public static LottoDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LottoDataBase.class, "Lotto.db")
                        .build();
            }
            return INSTANCE;
        }
    }



}
