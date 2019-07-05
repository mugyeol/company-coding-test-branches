package com.myapp.htpad.lotto.data;


import java.util.List;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@androidx.room.Dao
public interface LottoDao {



    @Query("SELECT * FROM lotto ORDER BY drwNo")
    List<LottoModel> getLotto();


    @Query("SELECT COUNT(*) FROM lotto WHERE drwtNo1 OR drwtNo2 OR drwtNo3 OR drwtNo4 OR drwtNo5 OR drwtNo6 OR bnusNo = :no")
    int getLottoByNo(String no);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLotto(LottoModel lottoModel);



}






