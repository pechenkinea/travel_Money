package com.pechenkin.travelmoney.utils;

import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.pechenkin.travelmoney.MainActivity;
import com.pechenkin.travelmoney.R;

public enum MemberIcons {


    MALE(0, R.drawable.ic_human_male_20x7),
    FEMALE(1, R.drawable.ic_human_female_20x9),
    MAN(2, R.drawable.ic_man_20x10),
    HIKING(3, R.drawable.ic_hiking_24),
    HAI(4, R.drawable.ic_hai_20x15),
    FAT(5, R.drawable.ic_human_fat_20x12);

    private final int icon;
    private final int id;

    MemberIcons(int id, int icon) {
        this.icon = icon;
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public static Drawable getIconById(int id) {

        for (MemberIcons m : MemberIcons.values()) {
            if (m.id == id) {
                return AppCompatResources.getDrawable(MainActivity.INSTANCE, m.icon);
            }
        }
        return AppCompatResources.getDrawable(MainActivity.INSTANCE, R.drawable.ic_human_male_20x7);
    }
}
