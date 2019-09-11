package com.pechenkin.travelmoney.bd.local;

import android.database.Cursor;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.TripManager;
import com.pechenkin.travelmoney.bd.local.query.IdTableRow;
import com.pechenkin.travelmoney.bd.local.table.Namespace;

import java.util.Date;


/**
 * Created by pechenkin on 04.04.2018.
 * Трата из локальной БД
 */
@Deprecated
public class CostLocal extends IdTableRow {

    public final String comment;
    public final String image_dir;
    public final Date date;
    public final Member member;
    public final Member to_member;
    public final boolean active;
    public final boolean repayment;

    public final double sum;


    public CostLocal(Cursor c) {
        super(c);

        this.comment = getStringColumnValue(Namespace.FIELD_COMMENT, c);
        this.sum = getDoubleColumnValue(Namespace.FIELD_SUM, c);
        this.image_dir = getStringColumnValue(Namespace.FIELD_IMAGE_DIR, c);
        this.date = getDateColumnValue(Namespace.FIELD_DATE, c);

        this.member = getMemberById(getLongColumnValue(Namespace.FIELD_MEMBER, c));
        this.to_member = getMemberById(getLongColumnValue(Namespace.FIELD_TO_MEMBER, c));

        this.active = getLongColumnValue(Namespace.FIELD_ACTIVE, c) == 1;
        this.repayment = getLongColumnValue(Namespace.FIELD_REPAYMENT, c) == 1;
    }

    private Member getMemberById(long id){
        return TripManager.INSTANCE.getActiveTrip().getAllMembers().First(member -> member.getId() == id);
    }




}