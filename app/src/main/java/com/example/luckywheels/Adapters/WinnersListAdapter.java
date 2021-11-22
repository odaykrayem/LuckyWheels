package com.example.luckywheels.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.luckywheels.Models.WinnerModel;
import com.example.luckywheels.R;

import java.util.ArrayList;
public class WinnersListAdapter extends RecyclerView.Adapter<WinnersListAdapter.ViewHolder> {


    // variable for our array list and context.
    private ArrayList<WinnerModel> winnerModelArrayList;
    private Context context;

    // creating a constructor.
    public WinnersListAdapter(ArrayList<WinnerModel> userModalArrayList, Context context) {
        this.winnerModelArrayList = userModalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.item_winners_list_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // getting data from our array list in our modal class.
        WinnerModel winnerModel = winnerModelArrayList.get(position);

        // on below line we are setting data to our text view.
        holder.nameTV.setText(winnerModel.getUserName());
        holder.prizeTV.setText(winnerModel.getPrize());
        holder.dateTV.setText(winnerModel.getDate());

        //choosing icon for the position
//        switch (position){
//            case 0:
//        }

    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return winnerModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating a variable for our text view and image view.
        private TextView nameTV, prizeTV, dateTV;
        private ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our variables.
            nameTV = itemView.findViewById(R.id.tv_item_name);
            prizeTV = itemView.findViewById(R.id.tv_item_prize);
            dateTV = itemView.findViewById(R.id.tv_item_date);
            icon = itemView.findViewById(R.id.iv_item_icon);
        }
    }
}