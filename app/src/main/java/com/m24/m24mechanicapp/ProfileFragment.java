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
import java.util.List;


public class ProfileFragment extends Fragment {
    ImageView propic;
    Button editphoto,editsubmit;
    EditText name,storename,place,dist,pin,licno,phone,email;
    String sname,sstorename,splace,sdist,spin,slicno,sphone,semail,imageurl;
    RecyclerView serviceedit;
    private FirebaseFirestore db ;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirestoreRecyclerAdapter serviceadp;
    private static final int PICK_IMAGE_REQUEST = 1;
    SharedPreferences systemfile;
    private String Uid;
    Uri image,downloaduri;


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
        db.collection("MechanicData").document(Uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                imageurl = (String) documentSnapshot.get("profileimage");
                Glide.with(getActivity()).load(imageurl).into(propic);
                name.setText((CharSequence) documentSnapshot.get("name"));
                storename.setText((CharSequence) documentSnapshot.get("storename"));
                place.setText((CharSequence) documentSnapshot.get("place"));
                dist.setText((CharSequence) documentSnapshot.get("dist"));
                pin.setText((CharSequence) documentSnapshot.get("pin"));
                licno.setText((CharSequence) documentSnapshot.get("licno"));
                phone.setText((CharSequence) documentSnapshot.get("phone"));
            }
        });
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
        Query query = db.collection("M24Assets").document("servicesprovided")
                .collection("services").orderBy("priority");
        FirestoreRecyclerOptions<serviceadapter> service = new FirestoreRecyclerOptions.Builder<serviceadapter>()
                .setQuery(query,serviceadapter.class).build();
        serviceadp = new FirestoreRecyclerAdapter<serviceadapter, register.serviceholder>(service) {
            @NonNull
            @NotNull
            @Override
            public register.serviceholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicerecycler, parent, false);
                return new register.serviceholder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull register.serviceholder holder, int position, @NonNull @NotNull serviceadapter model) {
                Glide.with(getActivity()).load(model.iconsurl).into(holder.icon);
                holder.serviceid.setText(model.service);
            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }
        };
        serviceedit.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        serviceedit.setLayoutManager(layoutManager);
        serviceedit.setAdapter(serviceadp);

        return v;
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