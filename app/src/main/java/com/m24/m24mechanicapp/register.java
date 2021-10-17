
package com.m24.m24mechanicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class register extends Fragment {
    EditText name,store,place,district,licno,phoneno,otp1f,otp2f,otp3f,password,conformpassword,email,pin;
    String sname,sstore,splace,sdistrict,spin,slicno,sphoneno,semail,spassword,sconformpassword;
    Button sub,done,setpassword;
    ImageButton back;
    SharedPreferences systemfile;
    RecyclerView recyclerViewservice;
    private List<String> selectedservices;
    int i,j=0;
    static String [] selected = new  String[10];
    LinearLayout servicelayouts;
    ConstraintLayout forms,otps,setpass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirestoreRecyclerAdapter serviceadp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        otps = view.findViewById(R.id.otp);
        setpass = view.findViewById(R.id.passwordscreen);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        store = view.findViewById(R.id.storename);
        place = view.findViewById(R.id.place);
        district = view.findViewById(R.id.dist);
        pin = view.findViewById(R.id.pin);
        licno = view.findViewById(R.id.licence);
        phoneno = view.findViewById(R.id.phone);
        otp1f = view.findViewById(R.id.otp1);
        otp2f = view.findViewById(R.id.otp2);
        otp3f = view.findViewById(R.id.otp3);
        password = view.findViewById(R.id.Password);
        conformpassword = view.findViewById(R.id.conformpassword);
        sub = view.findViewById(R.id.submit);
        back = view.findViewById(R.id.prevoius);
        done = view.findViewById(R.id.done);
        setpassword = view.findViewById(R.id.setpassword);
        recyclerViewservice = view.findViewById(R.id.service);
        servicelayouts =view.findViewById(R.id.servicelayout);
        forms = view.findViewById(R.id.form);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forms.setVisibility(View.VISIBLE);
                servicelayouts.setVisibility(View.GONE);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semail = email.getText().toString().trim();
                sname = name.getText().toString().trim();
                sstore = store.getText().toString().trim();
                splace = place.getText().toString().trim();
                sdistrict = district.getText().toString().trim();
                spin = pin.getText().toString().trim();
                slicno = licno.getText().toString().trim();
                sphoneno = phoneno.getText().toString().trim();

                if (semail.isEmpty() || sname.isEmpty() || sstore.isEmpty() || splace.isEmpty() || sdistrict.isEmpty() || slicno.isEmpty() || sphoneno.isEmpty() || spin.isEmpty()){
                    Toast.makeText(getActivity(), "Please fill the the form,please", Toast.LENGTH_SHORT).show();
                }
                else{
                    forms.setVisibility(View.GONE);
                    servicelayouts.setVisibility(View.VISIBLE);
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                servicelayouts.setVisibility(View.GONE);
                setpass.setVisibility(View.VISIBLE);

            }
        });
        setpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spassword = password.getText().toString().trim();
                sconformpassword = conformpassword.getText().toString().trim();
                Toast.makeText(getActivity(), "in", Toast.LENGTH_SHORT).show();
                try {
                    if (spassword.equals(sconformpassword) ){
                       mAuth.createUserWithEmailAndPassword(semail,spassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {
                              String Uid = authResult.getUser().getUid();
                               Toast.makeText(getContext(), Uid, Toast.LENGTH_SHORT).show();
                               selectedservices = Arrays.asList(selected);
                               mechdataupdater update = new mechdataupdater(sname,sstore,splace,sdistrict,spin,slicno,sphoneno,semail,selectedservices);
                               db.collection("MechanicData").document(Uid).set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      systemfile = getContext().getSharedPreferences("systemfile", Context.MODE_PRIVATE);
                                      SharedPreferences.Editor editor = systemfile.edit();
                                      editor.putString("uid",Uid);
                                      editor.commit();
                                      Intent intent =new Intent(getActivity(),Mainscreen.class);
                                      startActivity(intent);
                                      getActivity().finish();
                                  }
                                 }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {

                                  }
                              });

                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Toast.makeText(getActivity(),e.toString(), Toast.LENGTH_SHORT).show();
                           }
                       });
                    }
                    else {
                        Toast.makeText(getActivity(), "Check Passwords", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception  e){
                    Toast.makeText(getActivity(),e.toString() , Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///////////////////////////////////////////////////serviceblock//////////////////////////////////////////////////////////////////////
        Query query = db.collection("M24Assets").document("servicesprovided")
                .collection("services").orderBy("priority");
        FirestoreRecyclerOptions<serviceadapter> service = new FirestoreRecyclerOptions.Builder<serviceadapter>()
                .setQuery(query,serviceadapter.class).build();
        serviceadp = new FirestoreRecyclerAdapter<serviceadapter,serviceholder>(service) {
            @NonNull
            @org.jetbrains.annotations.NotNull
            @Override
            public serviceholder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicerecycler, parent, false);
                return new  serviceholder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull register.serviceholder holder, int position, @NonNull @org.jetbrains.annotations.NotNull serviceadapter model) {
                Glide.with(getActivity()).load(model.iconsurl).into(holder.icon);
                holder.serviceid.setText(model.service);
                holder.service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int length = selected.length,pos=-1,nullpos=-1;
                        String input = model.getService();
                       try {
                           for (i=0;i<length;i++){
                               if (selected[i]==input){
                                   pos=i;
                               }
                               if (selected[i]==null){
                                   nullpos=i;
                               }
                           }
                           if (pos != -1){
                               selected[pos]=null;
                               holder.cover.setBackgroundColor(Color.parseColor("#00EDE7E7"));
                           }
                           else if (nullpos != -1){
                               selected[nullpos]=input;
                               holder.cover.setBackgroundColor(Color.parseColor("#707070"));

                           }
                       }
                       catch (Exception e){
                           Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                       }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
        recyclerViewservice.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerViewservice.setLayoutManager(layoutManager);
        recyclerViewservice.setAdapter(serviceadp);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return view;
    }

    static class serviceholder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView serviceid;
        LinearLayout cover,service;
        public serviceholder(@NonNull @NotNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconview);
            serviceid = itemView.findViewById(R.id.servicename);
            cover = itemView.findViewById(R.id.layout);
            service = itemView.findViewById(R.id.servicelaypot);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        serviceadp.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        serviceadp.stopListening();
    }
}