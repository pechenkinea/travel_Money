package com.pechenkin.travelmoney.summry;

/**
 * Created by pechenkin on 13.06.2018.
 * Строка итога
 */

public class Summary {

    public final long member;
    public  final double sumIn;
    public  final double sumOut;

    public  Summary(long member, double sumIn, double sumOut)
    {
        this.member = member;
        this.sumIn = sumIn;
        this.sumOut = sumOut;
    }

}
