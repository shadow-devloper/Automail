package com.shadowDeveloper.automail.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shadowDeveloper.automail.MainActivity;
import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.ui.MessageFragment;
import com.shadowDeveloper.automail.ui.home.HomeFragment;
import com.shadowDeveloper.automail.ui.home.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.myviewholder>  {

    private Fragment fragment;

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

        String description;
        String desc = homeViewModels.get(position).getDesc();
        if (desc.length() < 50) {
            description = desc;
        } else {
            description = desc.substring(0, 50) + "...";
        }

        holder.img.setImageResource(homeViewModels.get(position).getImage());
        holder.sender.setText(homeViewModels.get(position).getSenderHeader());
        holder.date.setText(homeViewModels.get(position).getDate());
        holder.time.setText(homeViewModels.get(position).getTime());
        holder.header.setText(homeViewModels.get(position).getHeader());
        holder.desc.setText(description);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity=(AppCompatActivity)view.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.drawer_layout,new MessageFragment(
                                homeViewModels.get(position).getSenderHeader(),
                                homeViewModels.get(position).getSenderEmail(),
                                homeViewModels.get(position).getCc(),
                                homeViewModels.get(position).getBcc(),
                                homeViewModels.get(position).getDate(),
                                homeViewModels.get(position).getTime(),
                                homeViewModels.get(position).getHeader(),
                                homeViewModels.get(position).getDesc()))
                        .setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.nav_default_pop_enter_anim,R.anim.slide_down)
                        .addToBackStack(null).commit();
            }

        });


    }
    /*

    private void showBottomSheetDialog() {
        Context context = ;
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view);
    }

     */

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
