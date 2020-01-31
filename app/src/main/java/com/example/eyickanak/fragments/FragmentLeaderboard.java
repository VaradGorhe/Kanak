package com.example.eyickanak.fragments;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : FragmentLeaderboard.java
        Global Variables:None
        Functions   : addToList(),addToFinalList(),sortPoints().
 */


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eyickanak.R;
import com.example.eyickanak.adapter.LeaderboardAdapter;
import com.example.eyickanak.model.Leaderboard;
import com.example.eyickanak.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentLeaderboard extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;

    //For leaderboard to be diplayed,lists of userids and points is created that will be displayed in the app.
    private List<String> userIds = new ArrayList<>();
    private List<String> points = new ArrayList<>();
    private List<Leaderboard> leaderboardList = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragment_leaderboard, null);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progress);

        databaseReference = FirebaseDatabase.getInstance().getReference("Points");
        databaseReference.addValueEventListener(new ValueEventListener() {

            /*
            Here ,the path of the user's points is given.Points are fetched and the addToList()
            method is called.Till points are fetched,a progress bar will be displayed.
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    progressBar.setVisibility(View.VISIBLE);
                    addToList(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    /*
        Function Name:addToList()

        Logic:In this ,the user id and the points is fetched from the database.
              Initially,the count is 0.
              As the number of users and points increases,count will also increase.
              Now,this count is passes to another method called addToFinalList().
     */
    private void addToList(DataSnapshot children) {
        leaderboardList.clear();
        int count = 0;
        for(DataSnapshot dataSnapshot : children.getChildren()){
            userIds.add(dataSnapshot.getKey());
            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                points.add(snapshot.getValue().toString());
            }
            count ++;
        }
        addToFinalList(count);
    }


    /*
        Function Name:addToFinalList()

        Logic:The count is passed from the above method.
              The user will be fetched depending on the count.
              The user name and points will be fetched from database and will be passed
              to the leaderboardList.
              After this,sortPoints() function is called.
     */
    private void addToFinalList(final int count) {
        for(int i = 0; i < count; i++){
            databaseReference = FirebaseDatabase.getInstance().getReference("User");
            final int finalI = i;
            String userId = userIds.get(i);
            databaseReference.child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue().toString();
                    String point = points.get(finalI);

                    leaderboardList.add(new Leaderboard(point, name));
                    sortPoints();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    /*
        Function Name :sortPoints()

        Logic:The points are first converted into integer.
              Here,we have used Java Collections.
              It takes two values at a time and compares the two.
              Points are compared and accordingly are sorted.
              Sorting is done in descending order.
              And finally,the points which are sorted are then displayed on the leaderboardList.
              All the users will now be able to see their rank and the points in the
              Leaderboard Page of the app.
     */
    private void sortPoints() {
        Collections.sort(leaderboardList, new Comparator<Leaderboard>() {
            @Override
            public int compare(Leaderboard o1, Leaderboard o2) {
                int point1 = Integer.parseInt(o1.getPoints());
                int point2 = Integer.parseInt(o2.getPoints());

                return point2 - point1;
            }
        });
        adapter = new LeaderboardAdapter(leaderboardList, getContext());

        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
    }

}
