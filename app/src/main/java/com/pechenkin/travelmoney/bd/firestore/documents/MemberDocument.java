package com.pechenkin.travelmoney.bd.firestore.documents;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.firestore.FireBaseData;
import com.pechenkin.travelmoney.bd.firestore.MemberFireStore;
import com.pechenkin.travelmoney.bd.firestore.documents.cache.MemberLocalCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemberDocument {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static MemberDocument INSTANCE = null;
    public static MemberDocument getInstance(){
        if (INSTANCE == null){
            INSTANCE = new MemberLocalCache();
        }
        return INSTANCE;
    }

    public MemberDocument() {

    }


    public Member add(String tripUuid, String name, int color, int icon) {

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", name);
        data1.put("color", color);
        data1.put("icon", icon);
        String memberUuid = UUID.randomUUID().toString();

        DocumentReference memberRef = db.collection("trips").document(tripUuid).collection("members").document(memberUuid);
        memberRef.set(data1);

        return new MemberFireStore(name, color, icon, memberRef);
    }


    public List<Member> getAllMembersByUuidTrip(String tripUuid) {

        CollectionReference membersCollection = db.collection("trips").document(tripUuid).collection("members");

        QuerySnapshot queryDocumentSnapshots = FireBaseData.getSync(membersCollection.get());

        List<Member> memberDocuments = new ArrayList<>(queryDocumentSnapshots.size());
        for (DocumentSnapshot member : queryDocumentSnapshots.getDocuments()) {
            memberDocuments.add(new MemberFireStore(member));
        }

        return memberDocuments;
    }

}
