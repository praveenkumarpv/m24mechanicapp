package com.m24.m24mechanicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Helperclass.mechdataupdater;
import Adapters.serviceadapter;
import Adapters.brandadapter;
import Helperclass.servicehelperclass;
import Helperclass.verification;

import static android.content.Context.MODE_PRIVATE;


public class register extends Fragment {
    EditText name, store, place, district, licno, phoneno1, otp, password, conformpassword, pin;
    TextView locationtxtview;
    String sname, sstore, splace, sdistrict, spin, slicno, sphoneno1 = "", sphoneno2 = "",
            spassword, sconformpassword, verificationcode, hash,licence = "";
    Button sub, done, setpassword, donefrombrand, otpverify;
    ImageButton back, backfrombrand;
    SharedPreferences systemfile;
    RecyclerView recyclerViewservice, recyclerViewbrand;
    private List<String> selectedservices;
    private List<String> services = new ArrayList<>();
    private List<String> serviceicon = new ArrayList<>();
    private List<String> brand = new ArrayList<>();
    private List<String> verificatondata = new ArrayList<>();
    int i, j = 0;
    static String[] selected = new String[10];
    LinearLayout servicelayouts, brandlayout;
    ConstraintLayout forms, otplayout, setpass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private serviceadapter serviceadp;
    private brandadapter brandadp;


    Location location;
    Geocoder geocoder;
    private LocationManager locationManager;
    double lat, lng;
    private LocationRequest locationRequest;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());



        otplayout = view.findViewById(R.id.otp);
        setpass = view.findViewById(R.id.passwordscreen);
        name = view.findViewById(R.id.name);
        store = view.findViewById(R.id.storename);
        place = view.findViewById(R.id.place);
        district = view.findViewById(R.id.dist);
        pin = view.findViewById(R.id.pin);
        licno = view.findViewById(R.id.licence);
        phoneno1 = view.findViewById(R.id.phoneno1);
        locationtxtview = view.findViewById(R.id.locationtxtv);
        otp = view.findViewById(R.id.registerotp);
        otpverify = view.findViewById(R.id.verifyotp);
        password = view.findViewById(R.id.Password);
        conformpassword = view.findViewById(R.id.conformpassword);
        sub = view.findViewById(R.id.submit);
        back = view.findViewById(R.id.prevoius);
        backfrombrand = view.findViewById(R.id.backfrombrand);
        done = view.findViewById(R.id.done);
        donefrombrand = view.findViewById(R.id.doneinbrand);
        setpassword = view.findViewById(R.id.setpassword);
        recyclerViewservice = view.findViewById(R.id.service);
        recyclerViewbrand = view.findViewById(R.id.brand);
        servicelayouts = view.findViewById(R.id.servicelayout);
        brandlayout = view.findViewById(R.id.brandlayout);
        forms = view.findViewById(R.id.form);
        db.collection("M24data").document("services").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                servicehelperclass servicehelperclass = documentSnapshot.toObject(Helperclass.servicehelperclass.class);
                services = servicehelperclass.getServicename();
                serviceicon = servicehelperclass.getServiceimage();
                brand = servicehelperclass.getBrandname();
                serviceadp = new serviceadapter(services, serviceicon, getContext());
                brandadp = new brandadapter(brand, getContext());
//                String ser = String.valueOf(services.size());
//                Toast.makeText(getContext(), ser, Toast.LENGTH_SHORT).show();
                recyclerViewservice.setHasFixedSize(true);
                GridLayoutManager layoutManagerservice = new GridLayoutManager(getContext(), 3);
                recyclerViewservice.setLayoutManager(layoutManagerservice);
                recyclerViewservice.setAdapter(serviceadp);
                recyclerViewbrand.setHasFixedSize(true);
                GridLayoutManager layoutManagerbrand = new GridLayoutManager(getContext(), 2);
                recyclerViewbrand.setLayoutManager(layoutManagerbrand);
                recyclerViewbrand.setAdapter(brandadp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to get ads!", Toast.LENGTH_SHORT).show();
            }
        });
        locationtxtview.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                getpermission();
                locationenable();

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getpermission();

                }
                else{
                    locationManager = (LocationManager)getActivity().getSystemService(getContext().LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationenable();
                    } else {

                       // Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
                        fusedLocationProviderClient.getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            lat = location.getLatitude();
                                            lng = location.getLongitude();
                                            hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
                                            locationtxtview.setText(lat+","+lng);
                                        }
                                        else{
                                            Toast.makeText(getActivity(), "else", Toast.LENGTH_SHORT).show();
                                            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
                                        }
                                    }
                                });
                    }

                }

            }
        });

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
                sname = name.getText().toString().trim();
                sstore = store.getText().toString().trim();
                splace = place.getText().toString().trim();
                sdistrict = district.getText().toString().trim();
                spin = pin.getText().toString().trim();
                slicno = licno.getText().toString().trim();
                sphoneno1 = phoneno1.getText().toString().trim();

                if (sname.isEmpty() || sstore.isEmpty() || splace.isEmpty() || sdistrict.isEmpty() || slicno.isEmpty() || sphoneno1.isEmpty()  || spin.isEmpty() || hash.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill the the form,please", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("AAWKDATA").document("Licence").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {
                        verification verification = documentSnapshot.toObject(Helperclass.verification.class);
                        verificatondata = verification.getLicencenolist();
                        if (verificatondata.contains(slicno)){
                            //servicelayouts.setVisibility(View.VISIBLE);
                            //mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
                            db.collection("MechanicData").document(slicno).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    mechdataupdater mechdataupdater = documentSnapshot.toObject(Helperclass.mechdataupdater.class);
                                    if (! documentSnapshot.exists()){
                                        forms.setVisibility(View.GONE);
                                        otplayout.setVisibility(View.VISIBLE);
                                        PhoneAuthOptions options =
                                                PhoneAuthOptions.newBuilder(mAuth)
                                                        .setPhoneNumber("+91"+sphoneno1)       // Phone number to verify
                                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                                        .setActivity(getActivity())                 // Activity (for callback binding)
                                                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                                        .build();
                                        PhoneAuthProvider.verifyPhoneNumber(options);
                                    }
                                    else {
                                        Toast.makeText(getContext(), "Licence no already exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(getContext(), "You are not a member of AAWK", Toast.LENGTH_SHORT).show();
                        }
                     }
                 });

                }
            }
        });
        otpverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String manualotpcode = otp.getText().toString().trim();
                //Toast.makeText(getActivity(), "click", Toast.LENGTH_SHORT).show();
              if (!(manualotpcode.isEmpty()) && manualotpcode.length() == 6){
                  //Toast.makeText(getActivity(), "inside" , Toast.LENGTH_SHORT).show();
                  verifycode(manualotpcode);
              }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicelayouts.setVisibility(View.GONE);
                brandlayout.setVisibility(View.VISIBLE);
            }
        });
        donefrombrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brandlayout.setVisibility(View.GONE);
                setpass.setVisibility(View.VISIBLE);
            }
        });
        backfrombrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servicelayouts.setVisibility(View.VISIBLE);
                brandlayout.setVisibility(View.GONE);
            }
        });
        setpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spassword = password.getText().toString().trim();
                sconformpassword = conformpassword.getText().toString().trim();
                if (spassword.equals(sconformpassword)){
                    mechdataupdater update = new mechdataupdater(sname, sstore, splace, sdistrict, spin, slicno, sphoneno1,spassword,hash,"no","not paid","no",lat,lng,serviceadapter.getSelectedservices(),brandadapter.getSelectedbrandname());
                    db.collection("MechanicData").document(slicno).set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            systemfile =getActivity().getSharedPreferences("preference",MODE_PRIVATE);
                            SharedPreferences.Editor editor = systemfile.edit();
                            editor.putString("licenceno",slicno);
                            editor.commit();
                            Intent intent = new Intent(getActivity(), Mainscreen.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
               else {
                    Toast.makeText(getContext(), "Passwords not matching", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ///////////////////////////////////////////////////serviceblock//////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        locationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLastLocation();
                String l = String.valueOf(location.getLatitude());
                Toast.makeText(getContext(),"inside callback"+l+"," , Toast.LENGTH_SHORT).show();
                lat = location.getLatitude();
                lng = location.getLongitude();
                hash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
                locationtxtview.setText(lat+","+lng);
                stopLocationUpdates();
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Toast.makeText(getContext(),"inside locationavilability"+",", Toast.LENGTH_SHORT).show();

            }

        };
        return view;
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
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
                otplayout.setVisibility(View.GONE);
                servicelayouts.setVisibility(View.VISIBLE);
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
                otplayout.setVisibility(View.GONE);
                servicelayouts.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getpermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},110);
        }
    }
    private void locationenable() {
        locationManager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
        boolean gpsEnable = false;
        boolean networkEnable = false;

        try {
            gpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            networkEnable= locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!gpsEnable && !networkEnable){
            new AlertDialog.Builder(getActivity()).setTitle("Enable gps service").setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel",null).show();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

}