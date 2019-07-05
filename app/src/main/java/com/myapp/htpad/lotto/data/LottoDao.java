package com.myapp.htpad.lotto.data;


import java.util.List;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@androidx.room.Dao
public interface LottoDao {



    @Query("SELECT * FROM lotto ORDER BY drwNo")
    List<LottoModel> getLottos();


    @Query("SELECT COUNT(id) FROM lotto WHERE drwtNo1 =:num OR drwtNo2 =:num " +
            "OR drwtNo3=:num OR drwtNo4=:num OR drwtNo5=:num OR drwtNo6=:num OR bnusNo= :num")
    int countNum(int num);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLotto(LottoModel lottoModel);



}






