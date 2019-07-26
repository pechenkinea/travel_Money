package com.pechenkin.travelmoney;

public enum MemberIcons {

    MALE(0, R.drawable.ic_human_male_20x7),
    FEMALE(1, R.drawable.ic_human_female_20x9),
    MAN(2, R.drawable.ic_man_20x10),
    HIKING(3, R.drawable.ic_hiking_24),
    HAI(4, R.drawable.ic_hai_20x15),
    FAT(5, R.drawable.ic_human_fat_20x12);

    private int icon;
    private int id;

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

    public static int getIconById(int id) {

        for (MemberIcons m : MemberIcons.values()) {
            if (m.id == id) {
                return m.icon;
            }
        }
        return R.drawable.ic_human_male_20x7;
    }
}
