package com.pechenkin.travelmoney.test;

import com.pechenkin.travelmoney.bd.Member;

public class MemberForTest implements Member {

    private static int idCounter = 1;

    private final int id;
    private final int color;
    private final String name;

    public MemberForTest(String name, int color){
        this.id = idCounter++;
        this.name = name;
        this.color = color;
    }


    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int getColor() {
        return this.color;
    }

    @Override
    public int getIcon() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void edit(String name, int color, int icon) {

    }
}
