package com.myapp.htpad.lotto.data;

public class FrequencyModel {
    private int num;
    private int sum;

   public FrequencyModel(int num, int sum){
        this.num = num;
        this.sum =sum;
    }

    public int getNum() {
        return num;
    }

    public int getSum() {
        return sum;
    }
}
