package com.myapp.htpad.lotto.data;

import java.util.List;

public class LottoLocalDataSource implements LottoDataSource {

    private static  LottoLocalDataSource INSTANCE;

    private LottoDao mLottoDao;

    private LottoLocalDataSource(LottoDao lottoDao){
        mLottoDao = lottoDao;
    }

    public static LottoLocalDataSource getInstance(LottoDao lottoDao){
        if(INSTANCE==null){
            synchronized (LottoLocalDataSource.class){
                if (INSTANCE==null){
                    INSTANCE = new LottoLocalDataSource(lottoDao);
                }
            }
        }
        return INSTANCE;
    }

//    @Override
//    public synchronized void insertLotto(LottoModel lottoModel){
//       Runnable saveRunnable = () -> mLottoDao.insertLotto(lottoModel);
//        new Thread(saveRunnable).start();
//
//    }



    @Override
    public void getLottos(LoadDataCallback callback) {
        Runnable runnable = () -> {
            List<LottoModel> lotto =  mLottoDao.getLottos();
            if (lotto.isEmpty()){
                callback.onFail();
            }else{
                callback.onDataLoaded(lotto);
            }
        };

        new Thread(runnable).start();

    }

    @Override
    public synchronized void insertLotto(LottoModel lottoModel) {
       Runnable saveRunnable = () -> mLottoDao.insertLotto(lottoModel);
        new Thread(saveRunnable).start();
    }

    @Override
    public synchronized void countNums(int i, GetNumCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int sum =  mLottoDao.countNum(i);
                FrequencyModel frequencyModel = new FrequencyModel(i,sum);
                callback.onCounted(frequencyModel);
            }
        };
            new Thread(runnable).start();

    }
}
