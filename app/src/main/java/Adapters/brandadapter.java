package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.m24.m24mechanicapp.R;

import java.util.ArrayList;
import java.util.List;

public class brandadapter extends RecyclerView.Adapter<brandadapter.brandviewholder>  {

    List <String> brandname;
    static List <String> selectedbrandname = new ArrayList<>();
    List <String> selected = new ArrayList<>();
    Context context;

    public brandadapter(List<String> brandname, Context context) {
        this.brandname = brandname;
        this.context = context;
    }

    public brandadapter(List<String> brandname, List<String> selected, Context context) {
        this.brandname = brandname;
        this.selected = selected;
        this.context = context;
    }

    public static List<String> getSelectedbrandname() {
        return selectedbrandname;
    }

    public static void setSelectedbrandname(List<String> selectedbrandname) {
        brandadapter.selectedbrandname = selectedbrandname;
    }

    @NonNull
    @Override
    public brandviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.brandrecycler, parent, false);
        return new brandviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull brandviewholder holder, int position) {
        int pos = position;
        //Glide.with(context).load(serviceicon.get(pos)).into(holder.icon);
        holder.brandname.setText(brandname.get(pos));
        if (selected.isEmpty()){

        }
        else if (selected.contains(brandname.get(pos))){ ////code for if select array is empty
            selectedbrandname.add(brandname.get(pos));
            selected.remove(selected.indexOf(brandname.get(pos)));
            holder.brandcover.setBackgroundColor(Color.parseColor("#707070"));
        }
        holder.brandcover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedbrandname.contains(brandname.get(pos))){
                    selectedbrandname.remove( selectedbrandname.indexOf(brandname.get(pos)));
                    holder.brandcover.setBackgroundColor(Color.parseColor("#00EDE7E7"));
                }
                else{
                    selectedbrandname.add(brandname.get(pos));
                    holder.brandcover.setBackgroundColor(Color.parseColor("#707070"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandname.size();
    }

    public class brandviewholder extends RecyclerView.ViewHolder {
        TextView brandname;
        LinearLayout brandcover;
        public brandviewholder(@NonNull View itemView) {
            super(itemView);
            brandname = itemView.findViewById(R.id.brandname);
            brandcover = itemView.findViewById(R.id.brandcover);
        }
    }
}
