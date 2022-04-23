package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m24.m24mechanicapp.R;
import com.m24.m24mechanicapp.register;

import java.util.ArrayList;
import java.util.List;

public class serviceadapter extends RecyclerView.Adapter<serviceadapter.viewholder> {
    List<String> service;
    static List<String> selectedservices = new ArrayList<>();
    List<String> select = new ArrayList<>();
    List<String> serviceicon;
    Context context;

    public serviceadapter(List<String> service, List<String> serviceicon, Context context) {
        this.service = service;
        this.serviceicon = serviceicon;
        this.context = context;
    }

    public serviceadapter(List<String> service, List<String> select, List<String> serviceicon, Context context) {
        this.service = service;
        this.select = select;
        this.serviceicon = serviceicon;
        this.context = context;
    }

    public static List<String> getSelectedservices() {
        return selectedservices;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicerecycler, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        int pos = position;
       Glide.with(context).load(serviceicon.get(pos)).into(holder.icon);
        holder.serviceid.setText(service.get(pos));
        if (select.isEmpty()){

        }
        else if (select.contains(service.get(pos))){ ////code for if select array is empty
            selectedservices.add(service.get(pos));
            select.remove(select.indexOf(service.get(pos)));
            holder.cover.setBackgroundColor(Color.parseColor("#707070"));
        }
        holder.servicelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedservices.contains(service.get(pos))){
                        selectedservices.remove( selectedservices.indexOf(service.get(pos)));
                        holder.cover.setBackgroundColor(Color.parseColor("#00EDE7E7"));
                    }
                    else{
                        selectedservices.add(service.get(pos));
                        holder.cover.setBackgroundColor(Color.parseColor("#707070"));
                    }
            }
        });
    }
    @Override
    public int getItemCount() {
        return service.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView serviceid;
        LinearLayout cover,servicelayout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iconview);
            serviceid = itemView.findViewById(R.id.servicename);
            cover = itemView.findViewById(R.id.layout);
            servicelayout = itemView.findViewById(R.id.servicelaypot);
        }
    }
}
