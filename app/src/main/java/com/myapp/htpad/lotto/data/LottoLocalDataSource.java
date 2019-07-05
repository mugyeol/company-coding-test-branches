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
            List<LottoModel> lotto =  mLottoDao.getLotto();
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
}
