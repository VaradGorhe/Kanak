package com.example.eyickanak.fragments;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : FragmentPoints.java
        Global Variables:None
        Functions   : onDataChange()
 */



import android.opengl.ETC1;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eyickanak.R;
import com.example.eyickanak.adapter.SoilAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentPoints extends Fragment {
    TextView points;
    private RecyclerView recyclerViewSoil1, recyclerViewSoil2;


    //Variable of type String
    String point1 = null;
    String userId;

    //The soil moisture and temperature readings of both the bins is taken in an ArrayList.
    private RecyclerView.Adapter adapter;
    List<String> soilReading1 = new ArrayList<>();
    List<String> tempReading1 = new ArrayList<>();
    List<String> soilReading2 = new ArrayList<>();
    List<String> tempReading2 = new ArrayList<>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_fragment_points, null);

        points = view.findViewById(R.id.points);
        recyclerViewSoil1 = view.findViewById(R.id.recyclerViewSoil1);
        recyclerViewSoil1.setHasFixedSize(true);
        recyclerViewSoil1.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewSoil2 = view.findViewById(R.id.recyclerViewSoil2);
        recyclerViewSoil2.setHasFixedSize(true);
        recyclerViewSoil2.setLayoutManager(new LinearLayoutManager(getContext()));


        String id = FirebaseAuth.getInstance().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Points");
        if(databaseReference != null){

            /*
                Function name:onDataChange()

                Logic: The path of the user's points is being fetched here.In this onDataChange() method,
                initially.when the user registers,the points assigned to him will be 0.Therefore,
                in the app,it will be displayed 'Points not yet assigned'.
                When the composting process starts,points will be assigned and that will be fetched
                from the given path and will be displayed to the user in the app.

             */
            databaseReference.child(id).child("score").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() != null) {
                        point1 = dataSnapshot.getValue().toString();

                        if(point1.equals("0")){
                            points.setText("Points not yet assigned.");
                        } else {

                            points.setText("Your Points :-" + point1);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        /*
        Here,along with the points that will be displayed,the user will also be able to see the daily
        soil moisture and temperature readings.The logic of this method is same as that described above
        for the points.The path is first given,and then the readings are fetched from the given path.
        These readings are assigned to the adapter and then the adapter is passed into the recycler view.
        Inshort,the readings will be displayed in the Scroll view.

        The logic is same for both the bins.
         */
        soilReading1.clear();
        tempReading1.clear();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Status");
        databaseReference1.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child("Bin1").child("soil");
                for(DataSnapshot snapshot : dataSnapshot1.getChildren()){
                    soilReading1.add(snapshot.getValue().toString());
                }

                DataSnapshot dataSnapshot2 = dataSnapshot.child("Bin1").child("temp");
                for(DataSnapshot snapshot : dataSnapshot2.getChildren()){
                    tempReading1.add(snapshot.getValue().toString());
                }

                adapter = new SoilAdapter(soilReading1, tempReading1, getContext());
                recyclerViewSoil1.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        soilReading2.clear();
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Status");
        databaseReference2.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child("Bin2").child("soil");
                for(DataSnapshot snapshot : dataSnapshot1.getChildren()){
                    soilReading2.add(snapshot.getValue().toString());
                }

                DataSnapshot dataSnapshot2 = dataSnapshot.child("Bin2").child("temp");
                for(DataSnapshot snapshot : dataSnapshot2.getChildren()){
                    tempReading2.add(snapshot.getValue().toString());
                }

                adapter = new SoilAdapter(soilReading2, tempReading2, getContext());
                recyclerViewSoil2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        return view;
    }
}
