package com.shadowDeveloper.automail.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.ui.home.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.myviewholder> {

    ArrayList<HomeViewModel> homeViewModels;

    public HomeAdapter(ArrayList<HomeViewModel> homeViewModels) {
        this.homeViewModels = homeViewModels;
    }



    @NonNull
    @NotNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_design,parent,false);

        return new myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull myviewholder holder, int position) {
        holder.img.setImageResource(homeViewModels.get(position).getImage());
        holder.sender.setText(homeViewModels.get(position).getSender());
        holder.date.setText(homeViewModels.get(position).getDate());
        holder.time.setText(homeViewModels.get(position).getTime());
        holder.header.setText(homeViewModels.get(position).getHeader());
        holder.desc.setText(homeViewModels.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return homeViewModels.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView sender,date,time,header,desc;

        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img1);
            sender=itemView.findViewById(R.id.text1);
            date=itemView.findViewById(R.id.datetext);
            time=itemView.findViewById(R.id.timetext);
            header=itemView.findViewById(R.id.text2);
            desc=itemView.findViewById(R.id.text3);

        }
    }





}
