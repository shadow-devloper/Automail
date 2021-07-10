package com.shadowDeveloper.automail.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.ui.MessageSentFragment;
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


        String description;
        String desc = historyViewModels.get(position).getDesc();
        if (desc.length() < 50) {
            description = desc;
        } else {
            description = desc.substring(0, 50) + "...";
        }

        holder.img.setImageResource(historyViewModels.get(position).getImage());
        holder.rec.setText(historyViewModels.get(position).getRecHeader());
        holder.date.setText(historyViewModels.get(position).getDate());
        holder.time.setText(historyViewModels.get(position).getTime());
        holder.header.setText(historyViewModels.get(position).getHeader());
        holder.desc.setText(description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity=(AppCompatActivity)view.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drawer_layout,new MessageSentFragment(
                                historyViewModels.get(position).getRecHeader(),
                                historyViewModels.get(position).getRecEmail(),
                                historyViewModels.get(position).getCc(),
                                historyViewModels.get(position).getBcc(),
                                historyViewModels.get(position).getDate(),
                                historyViewModels.get(position).getTime(),
                                historyViewModels.get(position).getHeader(),
                                historyViewModels.get(position).getDesc()))
                        .setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.nav_default_pop_enter_anim,R.anim.slide_down)
                        .addToBackStack(null).commit();
            }

        });


    }

    @Override
    public int getItemCount() {
        return historyViewModels.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {

        ImageView img;
        TextView rec,date,time,header,desc;

        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img1);
            rec=itemView.findViewById(R.id.text1);
            date=itemView.findViewById(R.id.datetext);
            time=itemView.findViewById(R.id.timetext);
            header=itemView.findViewById(R.id.text2);
            desc=itemView.findViewById(R.id.text3);


        }
    }





}
