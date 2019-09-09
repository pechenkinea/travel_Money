package com.pechenkin.travelmoney.bd.firestore.documents;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pechenkin.travelmoney.bd.Member;
import com.pechenkin.travelmoney.bd.firestore.FireBaseData;
import com.pechenkin.travelmoney.bd.firestore.MemberFireStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class MemberDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static MemberDocument INSTANCE = new MemberDocument();

    private MemberDocument() {

    }


    public Member add(String tripUuid, String name, int color, int icon) {

        Map<String, Object> data1 = new HashMap<>();
        data1.put("name", name);
        data1.put("color", color);
        data1.put("icon", icon);
        String memberUuid = UUID.randomUUID().toString();

        CountDownLatch done = new CountDownLatch(1);

        DocumentReference memberRef = db.collection("trips").document(tripUuid).collection("members").document(memberUuid);

        memberRef.set(data1);
        return new MemberFireStore(name, color, icon, memberUuid, memberRef);
    }


    public List<Member> getAllMembersByUuidTrip(String tripUuid) {

        List<Member> memberDocuments = new ArrayList<>();
        CollectionReference membersCollection = db.collection("trips").document(tripUuid).collection("members");

        QuerySnapshot queryDocumentSnapshots = FireBaseData.getSync(membersCollection.get());

        for (DocumentSnapshot member : queryDocumentSnapshots.getDocuments()) {
            memberDocuments.add(new MemberFireStore(member));
        }

        return memberDocuments;
    }

}
