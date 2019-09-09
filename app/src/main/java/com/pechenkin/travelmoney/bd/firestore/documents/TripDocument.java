package com.pechenkin.travelmoney.bd.firestore.documents;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TripDocument {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static TripDocument INSTANCE = new TripDocument();

    private TripDocument() {

    }

    public void add(String uuid) {
        Map<String, Object> data1 = new HashMap<>();
        db.collection("trips").document(uuid).set(data1);
    }

}
