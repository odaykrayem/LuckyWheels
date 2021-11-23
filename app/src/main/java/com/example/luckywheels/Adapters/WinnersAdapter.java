package com.example.luckywheels.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.luckywheels.Models.ParticipantModel;
import com.example.luckywheels.R;

import java.util.ArrayList;

public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.ViewHolder> {

    // variable for our array list and context.
    private ArrayList<ParticipantModel> participantModelArrayList;
    private Context context;

    // creating a constructor.
    public WinnersAdapter(ArrayList<ParticipantModel> participantModelArrayList, Context context) {
        this.participantModelArrayList = participantModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_winners, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ParticipantModel participantModel = participantModelArrayList.get(position);

        holder.userNameTV.setText(participantModel.getUser_name().trim());
        holder.emailTV.setText(participantModel.getEmail().trim().substring(0,5)+"********");
        holder.prizeTV.setText(String.valueOf(participantModel.getPrize()));

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return participantModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView userNameTV, emailTV, prizeTV;
        private Button makeNotWinnerBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            // initializing our variables.
            userNameTV = itemView.findViewById(R.id.tv_item_name);
            emailTV = itemView.findViewById(R.id.tv_item_email);
            prizeTV = itemView.findViewById(R.id.tv_item_prize);

        }
    }
}
