package com.m24.m24mechanicapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import Adapters.serviceadapter;
import Helperclass.Newworkadpter;
import de.hdodenhof.circleimageview.CircleImageView;


public class homeFragment extends Fragment {
    ImageView proimage,noteicon,histicon;
    RecyclerView adview,work;
    String Uid;
    private FirebaseFirestore db ;
    FirestoreRecyclerAdapter workadapter;

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
                .collection("works");
         FirestoreRecyclerOptions<Newworkadpter> options = new FirestoreRecyclerOptions.Builder<Newworkadpter>()
                        .setQuery(query, Newworkadpter.class)
                        .build();
        workadapter = new FirestoreRecyclerAdapter<Newworkadpter,workholder>(options){

            @NonNull
            @Override
            public workholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomingwork, parent, false);
                return new workholder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull workholder holder, int position, @NonNull Newworkadpter model) {
             Glide.with(getContext()).load(model.getProfilephoto()).into(holder.proimage);
             holder.name.setText(model.getName());
            }
        };
        work.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        work.setLayoutManager(layoutManager);
        work.setAdapter(workadapter);
        return v;

    }

    private class workholder extends RecyclerView.ViewHolder {
        CircleImageView proimage;
        TextView name;
        public workholder(@NonNull View itemView) {
            super(itemView);
            proimage = itemView.findViewById(R.id.prophoto);
            name = itemView.findViewById(R.id.name);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        workadapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        workadapter.stopListening();
    }

}