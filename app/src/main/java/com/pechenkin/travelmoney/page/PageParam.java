package com.pechenkin.travelmoney.page;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.Trip;
import com.pechenkin.travelmoney.transaction.draft.DraftTransaction;

/**
 * Created by pechenkin on 20.04.2018.
 */

public class PageParam {

    public PageParam() {
    }

    private Uri uri = null;
    private int fragmentId = 0;
    private DraftTransaction draftTransaction = null;
    private Trip trip = null;
    private Member member = null;
    private Class<? extends Page> backPage = null;

    public PageParam setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
        return this;
    }

    public PageParam setMember(Member member) {
        this.member = member;
        return this;
    }

    public PageParam setDraftTransaction(DraftTransaction draftTransaction) {
        this.draftTransaction = draftTransaction;
        return this;
    }



    public PageParam setTrip(Trip trip) {
        this.trip = trip;
        return this;
    }

    public PageParam setBackPage(Class<? extends Page> backPage) {
        this.backPage = backPage;
        return this;
    }

    public PageParam setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    @NonNull
    public DraftTransaction getDraftTransaction() {
        if (draftTransaction == null) {
            draftTransaction = new DraftTransaction();
        }
        return draftTransaction;
    }


    public Trip getTrip() {
        return trip;
    }

    public Class<? extends Page> getBackPage() {
        return backPage;
    }

    public Member getMember() {
        return member;
    }

    public Uri getUri() {
        return uri;
    }
}
