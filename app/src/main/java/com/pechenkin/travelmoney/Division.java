package com.pechenkin.travelmoney;


/**
 * позволяет разделить сумму на несколько участников, что бы не потерялись копейки.
 * например 100/3 = 33+33+34
 */
public class Division {

    private int sum;
    private int count;

    public Division(int sum, int count) {
        if (sum < 0 || count < 1) {
            throw new RuntimeException("не могу поделить " + sum + " на " + count + " участников");
        }
        this.sum = sum;
        this.count = count;
    }

    public int getNext() {
        if (sum <= 0){
            return 0;
        }

        if (count == 1) {
            return sum;
        }

        int result = sum / count;
        sum -= result;
        count--;

        return result;
    }
}
