package com.myapp.htpad.lotto.data;

public class FrequencyModel implements Comparable<FrequencyModel> {
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

    @Override
    public int compareTo(FrequencyModel o) {
       if (this.sum > o.getSum()) {
        return -1;
    } else if (this.sum < o.getSum()) {
        return 1;
    }
        return 0;


    }
}
