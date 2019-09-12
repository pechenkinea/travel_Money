package com.pechenkin.travelmoney.bd.firestore.documents.cache;

import com.google.firebase.firestore.DocumentReference;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.firestore.MemberFireStore;
import com.pechenkin.travelmoney.bd.firestore.documents.MemberDocument;
import com.pechenkin.travelmoney.bd.local.table.TableMembers;

import java.util.ArrayList;
import java.util.List;

public class MemberLocalCache extends MemberDocument {

    @Override
    public Member add(String tripUuid, String name, int color, int icon) {
        Member member = super.add(tripUuid, name, color, icon);

        TableMembers.INSTANCE.add(name, color, icon, tripUuid, member.getUuid());

        return member;
    }


    @Override
    public List<Member> getAllMembersByUuidTrip(String tripUuid) {

        Member[] tripMembersLocal = TableMembers.INSTANCE.getMemberByTripUuid(tripUuid);

        List<Member> memberDocuments = new ArrayList<>(tripMembersLocal.length);
        for (Member member : tripMembersLocal) {
            DocumentReference memberRef = db.collection("trips").document(tripUuid).collection("members").document(member.getUuid());
            memberDocuments.add(new MemberFireStore(member, memberRef));
        }

        return memberDocuments;
    }
}
