package com.myapp.htpad.lotto.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lotto")
public class LottoModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "returnValue")
    @Ignore
    private String returnValue;
    @ColumnInfo(name = "drwNo")
    private int drwNo;
    @ColumnInfo(name = "drwtNo1")
    private int drwtNo1;
    @ColumnInfo(name = "drwtNo2")
    private int drwtNo2;
    @ColumnInfo(name = "drwtNo3")
    private int drwtNo3;
    @ColumnInfo(name = "drwtNo4")
    private int drwtNo4;
    @ColumnInfo(name = "drwtNo5")
    private int drwtNo5;
    @ColumnInfo(name = "drwtNo6")
    private int drwtNo6;
    @ColumnInfo(name = "bnusNo")
    private int bnusNo;

    public LottoModel(int id, int drwNo, int drwtNo1, int drwtNo2
            , int drwtNo3, int drwtNo4, int drwtNo5, int drwtNo6, int bnusNo){

        mId = id;
        this.drwNo = drwNo;
        this.drwtNo1 = drwtNo1;
        this.drwtNo2 = drwtNo2;
        this.drwtNo3 = drwtNo3;
        this.drwtNo4 = drwtNo4;
        this.drwtNo5 = drwtNo5;
        this.drwtNo6 = drwtNo6;
        this.bnusNo = bnusNo;

    }


    @NonNull
    public int getId() {
        return mId;
    }

    public String getReturnValue() {
        return returnValue;
    }
    public int getDrwNo() {
        return drwNo;
    }
    public int getDrwtNo1() {
        return drwtNo1;
    }
    public int getDrwtNo2() {
        return drwtNo2;
    }
    public int getDrwtNo3() {
        return drwtNo3;
    }

    public int getDrwtNo4() {
        return drwtNo4;
    }
    public int getDrwtNo5() {
        return drwtNo5;
    }
    public int getDrwtNo6() {
        return drwtNo6;
    }
    public int getBnusNo() {
        return bnusNo;
    }

}
