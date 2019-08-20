package com.pechenkin.travelmoney.bd;

public interface Member {

    long getId();

    int getColor();

    int getIcon();

    String getName();

    void edit(String name, int color, int icon);
}
