package com.m24.m24mechanicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class loginfragment extends Fragment {
    EditText email,password;
    TextView forget;
    Button subm;
    String id,pass;
    SharedPreferences systemfile;
    private FirebaseAuth mAuth;
    int Switch= 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view =  inflater.inflate(R.layout.fragment_loginfragment, container, false);
        email=view.findViewById(R.id.emailid);
        password=view.findViewById(R.id.password);

        forget=view.findViewById(R.id.forgetpassword);
        subm=view.findViewById(R.id.submit);
        systemfile = getContext().getSharedPreferences("systemfile",Context.MODE_PRIVATE);
        subm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Switch == 0) {
                    id = email.getText().toString();
                    pass = password.getText().toString();
                    if (id.isEmpty() && pass.isEmpty()) {
                        Toast.makeText(getContext(), "oh,I can't getyou", Toast.LENGTH_SHORT).show();
                    } else if (id.isEmpty()) {
                        Toast.makeText(getContext(), "Enter your Id,please", Toast.LENGTH_SHORT).show();
                    } else if (pass.isEmpty()) {
                        Toast.makeText(getContext(), "Enter password,please", Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.signInWithEmailAndPassword(id, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String Uid = authResult.getUser().getUid();
                                SharedPreferences.Editor editor = systemfile.edit();
                                editor.putString("uid", Uid);
                                editor.commit();
                                Intent intent = new Intent(getActivity(), Mainscreen.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    id = email.getText().toString();
                    mAuth.sendPasswordResetEmail(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Link Send to the Email id", Toast.LENGTH_SHORT).show();
                            password.setVisibility(View.VISIBLE);
                            forget.setVisibility(View.VISIBLE);
                            subm.setText("Submit");
                            Switch=0;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Invalid Email id", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = email.getText().toString();
                password.setVisibility(View.GONE);
                forget.setVisibility(View.GONE);
                subm.setText("Reset");
                Switch=1;
                if (id.isEmpty()){
                    Toast.makeText(getActivity(), "Enter id ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}