package com.m24.m24mechanicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import Adapters.brandadapter;
import Adapters.serviceadapter;
import Helperclass.mechdataupdater;
import Helperclass.servicehelperclass;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    CircleImageView propic;
    Button editphoto, editsubmit;
    EditText name, storename, place, dist, pin, phone1;
    TextView plocatontv,licno;
    private String sname, sstorename, splace, sdist, spin, slicno, sphone1, phash, imageurl, password;
    RecyclerView serviceedit, brandedit;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    SharedPreferences systemfile;
    private String licencenopreference;
    Uri image, downloaduri;
    LocationRequest locationRequest;
    List<String> selectedservicesp;
    private List<String> servicesp = new ArrayList<>();
    private List<String> serviceiconp = new ArrayList<>();
    private List<String> selectedbrand = new ArrayList<>();


    Location location;
    Geocoder geocoder;
    private LocationManager locationManager;
    private double lat, lng;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PICK_IMAGE_REQUEST && data != null) ;
        image = data.getData();
        Glide.with(this).load(image).into(propic);
        StorageReference storageReference = storageRef.child("M24/Workers/propic/" + licencenopreference);
        storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloaduri = uri;
                        imageurl = downloaduri.toString();
                        db.collection("MechanicData").document(licencenopreference).update("propic", imageurl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Cant Update At this Moment ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), (CharSequence) e, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        getpermission();
        locationenable();
        storageRef = storage.getReference();
        systemfile = getContext().getSharedPreferences("preference", Context.MODE_PRIVATE);
        licencenopreference = systemfile.getString("licenceno", "");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);


        name = v.findViewById(R.id.pname);
        propic = v.findViewById(R.id.profilephoto);
        storename = v.findViewById(R.id.pstorename);
        place = v.findViewById(R.id.pplace);
        dist = v.findViewById(R.id.pdist);
        pin = v.findViewById(R.id.ppin);
        licno = v.findViewById(R.id.plicence);
        phone1 = v.findViewById(R.id.pphoneno1);
        plocatontv = v.findViewById(R.id.plocationtxtv);
        editphoto = v.findViewById(R.id.editprofilephoto);
        serviceedit = v.findViewById(R.id.serviceedit);
        brandedit = v.findViewById(R.id.brandrecyclerprofile);
        editsubmit = v.findViewById(R.id.submitprofile);
        db.collection("MechanicData").document(licencenopreference).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mechdataupdater mechupdater = documentSnapshot.toObject(mechdataupdater.class);
                imageurl = mechupdater.getPropic();
                Glide.with(getActivity()).load(imageurl).into(propic);
                name.setText(mechupdater.getName());
                storename.setText(mechupdater.getStorename());
                place.setText(mechupdater.getPlace());
                dist.setText(mechupdater.getDist());
                pin.setText(mechupdater.getPin());
                slicno = mechupdater.getLicno();
                licno.setText(slicno);
                phone1.setText(mechupdater.getPhone1());
                //phone2.setText(mechupdater.getPhone2());
                selectedservicesp = mechupdater.getService();
                selectedbrand = mechupdater.getSelectedbrand();
                password = mechupdater.getPassword();
                lng = mechupdater.getLongitude();
                lat = mechupdater.getLatitude();
                phash = mechupdater.getGeohashcode();
                plocatontv.setText(lat + "," + lng);
                if (!(selectedservicesp.isEmpty())) {
                    viewservice(selectedservicesp, selectedbrand);
                } else {
                    //
                }

                // String ser = String.valueOf(selectedservicesp.size());
                //Toast.makeText(getContext(), ser, Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(getContext(), selectedservicesp.size(), Toast.LENGTH_SHORT).show();
        editphoto.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        ///////////////////////////////////////////////////////////service/////////////////////////////////////////////

        plocatontv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                        .addLocationRequest(locationRequest);
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getpermission();
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationenable();
                    }
                }
                else{
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        locationenable();
                    }else{
                        fusedLocationClient.requestLocationUpdates(locationRequest,
                                locationCallback,
                                Looper.getMainLooper());
                    }

                   // Toast.makeText(getContext(), "in", Toast.LENGTH_SHORT).show();
                }

//                fusedLocationClient.getLastLocation()
//                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                // Got last known location. In some rare situations this can be null.
//                                if (location != null) {
//                                   // location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                                    // geocoder= new Geocoder(getActivity());
//                                    lat = location.getLatitude();
//                                    lng = location.getLongitude();
//                                    phash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
//                                    plocatontv.setText(lat+","+lng);
//                                }
//                            }
//                        });
            }
        });

        editsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // semail = email.getText().toString().trim();
                sname = name.getText().toString().trim();
                sstorename = storename.getText().toString().trim();
                splace = place.getText().toString().trim();
                sdist = dist.getText().toString().trim();
                spin = pin.getText().toString().trim();
                slicno = licno.getText().toString().trim();
                sphone1 = phone1.getText().toString().trim();
                //List<String> selectedservicesp = new ArrayList<>();
                selectedservicesp = serviceadapter.getSelectedservices();
                String sers = String.valueOf(selectedservicesp.size());
               // Toast.makeText(getContext(), sers, Toast.LENGTH_SHORT).show();
               // Toast.makeText(getContext(), selectedservicesp.size(), Toast.LENGTH_SHORT).show();
                if ( sname.isEmpty() || sstorename.isEmpty() || splace.isEmpty() || sdist.isEmpty() || slicno.isEmpty() || sphone1.isEmpty()
                        || spin.isEmpty() || phash.isEmpty() ) {
                    Toast.makeText(getActivity(), "Please fill the the form,please", Toast.LENGTH_SHORT).show();
                } else {
                    mechdataupdater update = new mechdataupdater(sname, sstorename, splace, sdist, spin, slicno, sphone1,password,phash,imageurl,"not paid","no",lat,lng,serviceadapter.getSelectedservices(),brandadapter.getSelectedbrandname());
                    db.collection("MechanicData").document(licencenopreference).set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Succesfully updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to updated", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {

                   // return;
                }
                for (Location location : locationResult.getLocations()) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    plocatontv.setText(lat+","+lng);
                    phash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
                    stopLocationUpdates();
                    //Toast.makeText(getContext(), "out", Toast.LENGTH_SHORT).show();
                }
            }
        };
        return v;
    }

    private void viewservice(List<String> selectedservicesp,List<String> selectedbrands) {
        db.collection("M24data").document("services").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                servicehelperclass servicehelperclass = documentSnapshot.toObject(Helperclass.servicehelperclass.class);
                servicesp = servicehelperclass.getServicename();
                serviceiconp = servicehelperclass.getServiceimage();
                selectedbrand = servicehelperclass.getBrandname();
                serviceadapter serviceadp = new serviceadapter(servicesp,selectedservicesp,serviceiconp,getContext());
                brandadapter brandadapterp = new brandadapter(selectedbrand,selectedbrands,getContext());
                String ser = String.valueOf(servicesp.size());
                Toast.makeText(getContext(), ser, Toast.LENGTH_SHORT).show();serviceedit.setHasFixedSize(true);
                GridLayoutManager layoutManagerservicep = new GridLayoutManager(getContext(), 3);
                serviceedit.setLayoutManager(layoutManagerservicep);
                serviceedit.setAdapter(serviceadp);
                GridLayoutManager layoutManagerbrandp = new GridLayoutManager(getContext(), 2);
                brandedit.setLayoutManager(layoutManagerbrandp);
                brandedit.setAdapter(brandadapterp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to get ads!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void getpermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},110);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }


}