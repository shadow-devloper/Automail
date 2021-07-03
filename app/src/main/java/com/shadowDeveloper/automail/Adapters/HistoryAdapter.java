package com.shadowDeveloper.automail.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.ui.history.HistoryViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.myviewholder> {

    ArrayList<HistoryViewModel> historyViewModels;

    public HistoryAdapter(ArrayList<HistoryViewModel> historyViewModels) {
        this.historyViewModels = historyViewModels;
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
        holder.img.setImageResource(historyViewModels.get(position).getImage());
        holder.header.setText(historyViewModels.get(position).getHeader());
        holder.desc.setText(historyViewModels.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return historyViewModels.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView header,desc;

        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.text1);
            desc=itemView.findViewById(R.id.text2);

        }
    }





}
