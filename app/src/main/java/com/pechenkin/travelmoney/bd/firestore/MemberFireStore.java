package com.pechenkin.travelmoney.bd.firestore;

import android.graphics.Color;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;
import com.pechenkin.travelmoney.utils.Help;

public class MemberFireStore implements Member {

    private static int idCounter = 0;

    private boolean active = true;
    private String name;
    private int color;
    private int icon;
    private final long id;
    private final DocumentReference reference;
    private final String uuid;

    public MemberFireStore(String name, int color, int icon, DocumentReference reference) {
        this.id = idCounter++;
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.reference = reference;
        this.uuid = reference.getId();
    }

    public MemberFireStore(Member member, DocumentReference reference) {
        this.id = member.getId();
        this.name = member.getName();
        this.color = member.getColor();
        this.icon = member.getIcon();
        this.active = member.isActive();
        this.uuid = member.getUuid();

        this.reference = reference;
    }


    public MemberFireStore(DocumentSnapshot member) {
        this.id = idCounter++;
        this.reference = member.getReference();
        this.name = Help.toString(member.get("name"), "");
        this.color = (int) Help.toLong(member.getLong("color"), Color.BLACK);
        this.icon = (int) Help.toLong(member.getLong("icon"), 0);

        this.active = Help.toBoolean(member.getBoolean("active"), true);

        this.uuid = member.getId();

    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void edit(String name, int color, int icon) {

        this.reference.update(
                "name", name,
                "color", color,
                "icon", icon
        );

        TableMembers.INSTANCE.edit(uuid, name, color, icon);

        this.name = name;
        this.color = color;
        this.icon = icon;

    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;

        this.reference.update("active", active);
        TableMembers.INSTANCE.setActive(this.uuid, active);

    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof MemberFireStore) {
            return uuid.equals(((MemberFireStore) obj).uuid);
        }
        return false;
    }
}
