package com.m24.m24mechanicapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class homeFragment extends Fragment {
    ImageView proimage,noteicon,histicon;
    RecyclerView adview,work;
    String Uid;
    private FirebaseFirestore db ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();
        SharedPreferences systemfile = getContext().getSharedPreferences("systemfile", Context.MODE_PRIVATE);
        Uid = systemfile.getString("uid","");
        proimage =v.findViewById(R.id.profileimage);
        noteicon = v.findViewById(R.id.notification);
        histicon = v.findViewById(R.id.history);
        adview = v.findViewById(R.id.ad);
        work = v.findViewById(R.id.newwork);
        noteicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_notificationFragment);
            }
        });
        histicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_historyFragment);
            }
        });
        proimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_profileFragment);
            }
        });
        db.collection("MechanicData").document(Uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Glide.with(getActivity()).load(documentSnapshot.get("profileimage")).into(proimage);
            }
        });
        //workrecycler///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Query query = db.collection("MechanicData").document(Uid)
                .collection("Works");

        return v;
    }
}