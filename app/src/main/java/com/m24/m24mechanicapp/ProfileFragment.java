package com.m24.m24mechanicapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import Adapters.serviceadapter;
import Helperclass.mechdataupdater;
import Helperclass.servicehelperclass;


public class ProfileFragment extends Fragment {
    ImageView propic;
    Button editphoto,editsubmit;
    EditText name,storename,place,dist,pin,licno,phone,email;
    String sname,sstorename,splace,sdist,spin,slicno,sphone,semail,imageurl;
    RecyclerView serviceedit;
    private FirebaseFirestore db ;
    FirebaseStorage storage;
    StorageReference storageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    SharedPreferences systemfile;
    private String Uid;
    Uri image,downloaduri;
    List<String> selectedservicesp;
    private List<String> servicesp = new ArrayList<>();
    private List<String> serviceiconp = new ArrayList<>();


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
           if (resultCode == PICK_IMAGE_REQUEST && data != null);
                image = data.getData();
        Glide.with(this).load(image).into(propic);
        StorageReference storageReference = storageRef.child("M24/Workers/propic/"+Uid);
        storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       downloaduri = uri;
                       imageurl = downloaduri.toString();
                       db.collection("MechanicData").document(Uid).update("profileimage",imageurl)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       Toast.makeText(getActivity(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull  Exception e) {
                               Toast.makeText(getActivity(), "Cant Update At this Moment ", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               });
               
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
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
        storageRef = storage.getReference();
        systemfile = getContext().getSharedPreferences("systemfile", Context.MODE_PRIVATE);
        Uid = systemfile.getString("uid","");
        name = v.findViewById(R.id.name);
        propic = v.findViewById(R.id.profilephoto);
        storename = v.findViewById(R.id.storename);
        place = v.findViewById(R.id.place);
        dist = v.findViewById(R.id.dist);
        pin = v.findViewById(R.id.pin);
        licno = v.findViewById(R.id.licence);
        phone = v.findViewById(R.id.phone);
        email = v.findViewById(R.id.email);
        editphoto = v.findViewById(R.id.editprofilephoto);
        serviceedit = v.findViewById(R.id.serviceedit);
        editsubmit = v.findViewById(R.id.submitprofile);
        db.collection("MechanicData").document(Uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mechdataupdater mechupdater = documentSnapshot.toObject(mechdataupdater.class);
                imageurl = (String) documentSnapshot.get("profileimage");
                Glide.with(getActivity()).load(imageurl).into(propic);
                name.setText(mechupdater.getName());
                storename.setText(mechupdater.getStorename());
                place.setText(mechupdater.getPlace());
                dist.setText(mechupdater.getDist());
                pin.setText(mechupdater.getPin());
                licno.setText(mechupdater.getLicno());
                phone.setText(mechupdater.getPhone());
                semail = mechupdater.getEmail();
                selectedservicesp = mechupdater.getService();
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
        db.collection("M24data").document("services").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                servicehelperclass servicehelperclass = documentSnapshot.toObject(Helperclass.servicehelperclass.class);
                servicesp = servicehelperclass.getServicename();
                serviceiconp = servicehelperclass.getServicename();
                //String sers = String.valueOf(selectedservicesp.size());
               // Toast.makeText(getContext(), sers, Toast.LENGTH_SHORT).show();
                serviceadapter serviceadp = new serviceadapter(servicesp,selectedservicesp,serviceiconp,getContext());
                String ser = String.valueOf(servicesp.size());
                Toast.makeText(getContext(), ser, Toast.LENGTH_SHORT).show();
                

                serviceedit.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
                serviceedit.setLayoutManager(layoutManager);
                serviceedit.setAdapter(serviceadp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failed to get ads!", Toast.LENGTH_SHORT).show();
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
                sphone = phone.getText().toString().trim();
                //List<String> selectedservicesp = new ArrayList<>();
                selectedservicesp = serviceadapter.getSelectedservices();
                String sers = String.valueOf(selectedservicesp.size());
                Toast.makeText(getContext(), sers, Toast.LENGTH_SHORT).show();
               // Toast.makeText(getContext(), selectedservicesp.size(), Toast.LENGTH_SHORT).show();
                if ( sname.isEmpty() || sstorename.isEmpty() || splace.isEmpty() || sdist.isEmpty() || slicno.isEmpty() || sphone.isEmpty()
                        || spin.isEmpty() ) {
                    Toast.makeText(getActivity(), "Please fill the the form,please", Toast.LENGTH_SHORT).show();
                } else {
                    mechdataupdater update = new mechdataupdater(sname, sstorename, splace, sdist, spin, slicno, sphone, semail,serviceadapter.getSelectedservices());
                    db.collection("MechanicData").document(Uid).set(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Succesfully updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        });

        return v;
    }



}