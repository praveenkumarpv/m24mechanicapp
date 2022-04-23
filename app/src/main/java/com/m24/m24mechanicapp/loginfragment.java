package com.m24.m24mechanicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

import Helperclass.mechdataupdater;

import static android.content.Context.MODE_PRIVATE;


public class loginfragment extends Fragment {
    ConstraintLayout loginform,otpbox,passwordrestbox;
    EditText Licenceno,password,otp,passwordreset,conformpasswordreset;
    TextView forget;
    Button submbt,conformotpbt,resetpasswordbt;
    String id="",pass,otptxt,passwordtxt,conformpasswordtxt,verificationcode;
    SharedPreferences systemfile;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    int Switch= 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        View view =  inflater.inflate(R.layout.fragment_loginfragment, container, false);
        loginform = view.findViewById(R.id.loginform);
        otpbox = view.findViewById(R.id.otploginbox);
        passwordrestbox = view.findViewById(R.id.resetpasswordscreen);

        Licenceno = view.findViewById(R.id.licenceno);
        password = view.findViewById(R.id.password);
        otp = view.findViewById(R.id.loginotp);
        passwordreset = view.findViewById(R.id.fPassword);
        conformpasswordreset = view.findViewById(R.id.fconformpassword);



        forget=view.findViewById(R.id.forgetpassword);

        submbt = view.findViewById(R.id.submit);
        conformotpbt = view.findViewById(R.id.lverifyotp);
        resetpasswordbt = view.findViewById(R.id.setpassword);

        systemfile = getContext().getSharedPreferences("systemfile",Context.MODE_PRIVATE);
        submbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Switch == 0) {
                    id = Licenceno.getText().toString();
                    pass = password.getText().toString();
                    if (id.isEmpty() && pass.isEmpty()) {
                        Toast.makeText(getContext(), "oh,I can't getyou", Toast.LENGTH_SHORT).show();
                    } else if (id.isEmpty()) {
                        Toast.makeText(getContext(), "Enter your Id,please", Toast.LENGTH_SHORT).show();
                    } else if (pass.isEmpty()) {
                        Toast.makeText(getContext(), "Enter password,please", Toast.LENGTH_SHORT).show();
                    } else {
                        getdocument();

                    }
                }
                else {
                    id = Licenceno.getText().toString();
                    if (! id.isEmpty()){
                      getdocument();
                      loginform.setVisibility(View.GONE);
                      otpbox.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Licenceno.setText("");
                password.setVisibility(View.GONE);
                forget.setVisibility(View.GONE);
                submbt.setText("Reset");
                Switch=1;
                if (id.isEmpty()){
                    Toast.makeText(getActivity(), "Enter id ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        conformotpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otptxt = otp.getText().toString().trim();
                if (!otptxt.isEmpty() && otptxt.length() == 6){
                    verifycode(otptxt);
                }
            }
        });
        resetpasswordbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              passwordtxt = passwordreset.getText().toString().trim();
              conformpasswordtxt = conformpasswordreset.getText().toString().trim();
              if (passwordtxt.equals(conformpasswordtxt)){
                  db.collection("MechanicData").document(id).update("password",passwordtxt).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                       Switch = 0;
                       pass = passwordtxt;
                       getdocument();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(getContext(), "Password reset failed!", Toast.LENGTH_SHORT).show();
                      }
                  });
              }
            }
        });
        return view;
    }

    private void getdocument() {
        db.collection("MechanicData").whereEqualTo("licno",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    mechdataupdater mechupdater = document.toObject(mechdataupdater.class);
                    if (Switch == 0){
                        if (pass.equals(mechupdater.getPassword())){
                            SharedPreferences systemfile =getActivity().getSharedPreferences("preference",MODE_PRIVATE);
                            SharedPreferences.Editor editor = systemfile.edit();
                            editor.putString("licenceno",mechupdater.getLicno());
                            editor.commit();
                            Intent intent = new Intent(getActivity(), Mainscreen.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else{
                            Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber("+91"+mechupdater.getPhone1())       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(getActivity())                 // Activity (for callback binding)
                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);
                    }

                }
            }
        });
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationcode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String smscode = phoneAuthCredential.getSmsCode();
            if (!smscode.equals(null)){
                otp.setText(smscode);
                verifycode(smscode);
                Toast.makeText(getContext(), "completed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
    };
    private void verifycode(String smscode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode,smscode);
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                otpbox.setVisibility(View.GONE);
                passwordrestbox.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

}