package com.myapp.htpad.lotto.view.main;

import android.content.Context;

import java.util.ArrayList;

public interface MainActivityContract {


    interface View{

        void onCreatedNum(ArrayList<Integer> numArray, String numStr);
        void onCheckedIfWin(boolean isWon);
        Context getContext();
        void showDialog(String title, String msg);
    }
    interface  Presenter{
        void attachView(View view);
        void detachView();
        void createNum(String numStr);
        void checkIfWin(int num, ArrayList<Integer>numset);
    }


}
