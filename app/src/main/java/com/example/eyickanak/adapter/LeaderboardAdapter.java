package com.example.eyickanak.adapter;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : LeaderBoardAdapter.java
        Global Variables:-
        Functions   : -

        Logic is explained in the FragmentLeaderboard.java file.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eyickanak.R;
import com.example.eyickanak.model.Leaderboard;
import com.example.eyickanak.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class  LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {
    List<Leaderboard> leaderboardList;
    Context context;

    public LeaderboardAdapter(List<Leaderboard> leaderboardList, Context context) {
        this.leaderboardList = leaderboardList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list_item, parent, false);
        return new LeaderboardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.rank.setText(String.valueOf(position + 1) + ".");
        holder.userName.setText(leaderboardList.get(position).getName());
        holder.leaderboardPoints.setText(leaderboardList.get(position).getPoints());
    }

    @Override
    public int getItemCount() {
        return leaderboardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView userName, leaderboardPoints, rank;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rank = itemView.findViewById(R.id.rank);
            userName = itemView.findViewById(R.id.userName);
            leaderboardPoints = itemView.findViewById(R.id.leaderboardPoints);
        }
    }
}
