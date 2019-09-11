package com.pechenkin.travelmoney.bd;

public interface Member {

    long getId();

    int getColor();

    int getIcon();

    String getName();

    String getUuid();

    void edit(String name, int color, int icon);

    void setActive(boolean active);

    boolean isActive();
}
